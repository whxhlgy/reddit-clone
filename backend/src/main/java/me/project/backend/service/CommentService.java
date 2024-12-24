package me.project.backend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Comment;
import me.project.backend.domain.CommentClosure;
import me.project.backend.domain.Post;
import me.project.backend.exception.notFound.CommentNotFoundException;
import me.project.backend.exception.notFound.PostNotFoundException;
import me.project.backend.payload.UserDetailsImpl;
import me.project.backend.payload.dto.CommentDTO;
import me.project.backend.repository.CommentClosureRepository;
import me.project.backend.repository.CommentRepository;
import me.project.backend.repository.PostRepository;
import me.project.backend.service.IService.ILikeService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CommentService {

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentClosureRepository closureRepository;
    private final ILikeService likeService;

    public CommentService(ModelMapper modelMapper, PostRepository postRepository, CommentRepository commentRepository, CommentClosureRepository commentClosureRepository, ILikeService likeService) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.closureRepository = commentClosureRepository;
        this.likeService = likeService;
    }

    public List<CommentDTO> findAllByPostId(long postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        List<CommentDTO> commentDTOs = new ArrayList<>();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        for (Comment comment : comments) {
            if (Objects.equals(comment.getParentId(), null)) {
                CommentDTO commentDTO = convertCommentToDTOWithReactionAndLikeCount(username, comment);
                buildTree(username, commentDTO, comments);
                commentDTOs.add(commentDTO);
            }
        }
        return commentDTOs;
    }

    public CommentDTO findAllByAncestorId(long ancestorId) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        Comment ancestor = commentRepository.findById(ancestorId).orElseThrow(() -> new CommentNotFoundException(ancestorId));
        List<Comment> descendants = commentRepository.findCommentByAncestorId(ancestorId);
        CommentDTO tree = convertCommentToDTOWithReactionAndLikeCount(username, ancestor);
        buildTree(username, tree, descendants);
        return tree;
    }

    private void buildTree(String username, CommentDTO ancestor, List<Comment> descendants) {
        for (Comment comment : descendants) {
            if (Objects.equals(comment.getParentId(), ancestor.getId())) {
                CommentDTO child = convertCommentToDTOWithReactionAndLikeCount(username, comment);
                buildTree(username, child, descendants);
                ancestor.getChildren().add(child);
            }
        }
    }

    @Transactional
    public CommentDTO save(long postId, Long parentId, CommentDTO commentDTO) {
        log.debug("Save comment with id: {}", commentDTO.getId());
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        comment.setPost(post);
        comment.setParentId(parentId);
        Comment savedComment = commentRepository.save(comment);
        saveWithClosure(parentId, savedComment);
        return modelMapper.map(savedComment, CommentDTO.class);
    }

    private void saveWithClosure(Long parentId, Comment comment) {
        // save selfClosure
        CommentClosure selfClosure = new CommentClosure(comment, comment, 0);
        closureRepository.save(selfClosure);

        // save ancestorClosure
        if (parentId != null) {
            Comment parentComment = commentRepository.findById(parentId).orElseThrow(() -> new CommentNotFoundException(parentId));
            List<CommentClosure> parentClosures = closureRepository.findAllByDescendant(parentComment);
            for (CommentClosure parentClosure : parentClosures) {
                CommentClosure commentClosure = new CommentClosure(parentClosure.getAncestor(), comment, parentClosure.getDistance() + 1);
                closureRepository.save(commentClosure);
            }
        }
    }

    private CommentDTO convertCommentToDTOWithReactionAndLikeCount(String username, Comment comment) {
        CommentDTO dto = modelMapper.map(comment, CommentDTO.class);
        dto.setReaction(likeService.getUserReactionByCommentId(username, comment.getId()));
        dto.setLikeCount(likeService.countLikeByCommentId(comment.getId()));
        return dto;
    }

}

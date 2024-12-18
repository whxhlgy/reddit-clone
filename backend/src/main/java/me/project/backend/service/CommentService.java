package me.project.backend.service;

import jakarta.transaction.Transactional;
import me.project.backend.domain.Comment;
import me.project.backend.domain.CommentClosure;
import me.project.backend.domain.Post;
import me.project.backend.exception.notFound.CommentNotFoundException;
import me.project.backend.exception.notFound.PostNotFoundException;
import me.project.backend.payload.dto.CommentDTO;
import me.project.backend.payload.dto.CommentTreeDTO;
import me.project.backend.repository.CommentClosureRepository;
import me.project.backend.repository.CommentRepository;
import me.project.backend.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentClosureRepository closureRepository;
    private final Integer GENERATION_NUMBER = 3;

    public CommentService(ModelMapper modelMapper, PostRepository postRepository, CommentRepository commentRepository, CommentClosureRepository commentClosureRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.closureRepository = commentClosureRepository;
    }

    public List<CommentDTO> findAllByPostId(int postId) {
        Post post = postRepository.findPostWithCommentsById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        List<Comment> comments = post.getComments();
        return modelMapper.map(comments, new TypeToken<List<CommentDTO>>() {}.getType());
    }

    @Transactional
    public CommentDTO save(long postId, Long parentId, CommentDTO commentDTO) {
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

    public CommentTreeDTO findAllByAncestor(long ancestorId) {
        Comment ancestor = commentRepository.findById(ancestorId).orElseThrow(() -> new CommentNotFoundException(ancestorId));
        List<CommentClosure> ancestorClosures = closureRepository.findALlByAncestor(ancestor);

        ArrayList<Comment> descendants = new ArrayList<>();
        for (CommentClosure ancestorClosure : ancestorClosures) {
            descendants.add(ancestorClosure.getDescendant());
        }

        CommentTreeDTO tree = new CommentTreeDTO();
        for (Comment comment : descendants) {
            if (Objects.equals(comment.getId(), ancestorId)) {
                tree.setId(comment.getId());
                tree.setContent(comment.getContent());
                tree.setChildren(new ArrayList<>());
                break;
            }
        }
        assert tree.getId() != null;
        return buildTree(tree, descendants, ancestorId);
    }

    private CommentTreeDTO buildTree(CommentTreeDTO tree, List<Comment> descendants, Long parentId) {
        for (Comment comment : descendants) {
            if (Objects.equals(comment.getParentId(), parentId)) {
                CommentTreeDTO subtree = new CommentTreeDTO();
                subtree.setChildren(new ArrayList<>());
                subtree.setId(comment.getId());
                subtree.setContent(comment.getContent());
                buildTree(subtree, descendants, comment.getId());
                tree.getChildren().add(subtree);
            }
        }

        return tree;
    }
}

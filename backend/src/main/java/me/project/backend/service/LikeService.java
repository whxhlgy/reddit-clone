package me.project.backend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Comment;
import me.project.backend.domain.LikeComment;
import me.project.backend.exception.ServiceException;
import me.project.backend.exception.ServiceRuntimeException;
import me.project.backend.exception.notFound.CommentNotFoundException;
import me.project.backend.payload.dto.LikeDTO;
import me.project.backend.repository.CommentRepository;
import me.project.backend.repository.LikeCommentRepository;
import me.project.backend.service.IService.ILikeService;
import me.project.backend.util.ContextUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LikeService implements ILikeService {

    private final LikeCommentRepository likeCommentRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;

    public LikeService(LikeCommentRepository likeCommentRepository, ModelMapper modelMapper, CommentRepository commentRepository) {
        this.likeCommentRepository = likeCommentRepository;
        this.modelMapper = modelMapper;
        this.commentRepository = commentRepository;
    }

    public int getUserReactionByCommentId(long commentId){
        log.debug("get reaction commentId:{}", commentId);
        Optional<String> username = ContextUtil.getUsername();
        if (username.isEmpty()) {
            return 0;
        }

        // check if comment exists
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));

        Optional<LikeComment> reaction = likeCommentRepository.findLikeCommentByUsernameAndComment(username.get(), comment);
        Integer r = reaction.map(LikeComment::getReaction).orElse(0);
        log.debug("username:{}, reaction: {}", username.get(), r);
        return r;
    }

    @Transactional
    public LikeDTO likeCommentByCommentId(long commentId, int reaction) {
        log.debug("Like a comment with commentId: {}, ", commentId);
        Optional<String> username = ContextUtil.getUsername();
        if (username.isEmpty()) {
            throw new ServiceRuntimeException("Cannot find username" +
                    "in an authenticated request, this is impossible, so there is must some thing bad happened");
        }
        log.debug("username:{}", username.get());

        // check if comment exists
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));

        // update or insert the like reaction
        Optional<LikeComment> likeCommentOptional = likeCommentRepository.findLikeCommentByUsernameAndComment(username.get(), comment);
        LikeComment likeComment1 = new LikeComment();
        if (likeCommentOptional.isEmpty()) {
            log.debug("Cannot find like record, insert one");
            LikeComment save = likeCommentRepository.save(LikeComment.builder()
                    .username(username.get())
                    .comment(comment)
                    .reaction(reaction)
                    .build());
            return modelMapper.map(save, LikeDTO.class);
        } else {
            log.debug("Updating reaction record");
            LikeComment likeComment = likeCommentOptional.get();
            likeComment.setReaction(reaction);
            LikeComment save = likeCommentRepository.save(likeComment);
            return modelMapper.map(save, LikeDTO.class);
        }
    }
}

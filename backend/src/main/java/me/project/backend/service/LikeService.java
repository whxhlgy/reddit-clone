package me.project.backend.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Comment;
import me.project.backend.domain.CommentLike;
import me.project.backend.exception.ServiceRuntimeException;
import me.project.backend.exception.notFound.CommentNotFoundException;
import me.project.backend.payload.dto.LikeDTO;
import me.project.backend.repository.CommentRepository;
import me.project.backend.repository.CommentLikeRepository;
import me.project.backend.service.IService.ILikeService;
import me.project.backend.util.ContextUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LikeService implements ILikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;

    public LikeService(CommentLikeRepository commentLikeRepository, ModelMapper modelMapper, CommentRepository commentRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.modelMapper = modelMapper;
        this.commentRepository = commentRepository;
    }

    public int getUserReactionByCommentId(long commentId){
        log.debug("get reaction commentId:{}", commentId);
        Optional<String> username = ContextUtil.getUsername();
        if (username.isEmpty()) {
            return 0;
        }

        Optional<CommentLike> reaction = commentLikeRepository.findCommentLikeByUsernameAndCommentId(username.get(), commentId);
        Integer r = reaction.map(CommentLike::getReaction).orElse(0);
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

        // important, avoid add a like to a not existed comment
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));

        // update or insert the like reaction
        Optional<CommentLike> likeCommentOptional = commentLikeRepository.findCommentLikeByUsernameAndCommentId(username.get(), commentId);
        if (likeCommentOptional.isEmpty()) {
            log.debug("Cannot find like record, insert one");
            CommentLike save = commentLikeRepository.save(CommentLike.builder()
                    .username(username.get())
                    .comment(comment)
                    .reaction(reaction)
                    .build());
            return modelMapper.map(save, LikeDTO.class);
        } else {
            log.debug("Updating reaction record");
            CommentLike commentLike = likeCommentOptional.get();
            commentLike.setReaction(reaction);
            CommentLike save = commentLikeRepository.save(commentLike);
            return modelMapper.map(save, LikeDTO.class);
        }
    }

    public int countLikeByCommentId(long commentId) {
        log.debug("countLike commentId:{}", commentId);
        return commentLikeRepository.sumCommentLikeByCommentId(commentId).orElse(0);
    }
}

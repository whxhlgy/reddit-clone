package me.project.backend.service;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.LikeComment;
import me.project.backend.repository.LikeCommentRepository;
import me.project.backend.service.IService.ILikeService;
import me.project.backend.util.ContextUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LikeService implements ILikeService {

    private final LikeCommentRepository likeCommentRepository;

    public LikeService(LikeCommentRepository likeCommentRepository) {
        this.likeCommentRepository = likeCommentRepository;
    }

    public int getUserReactionByCommentId(long commentId){
        log.debug("get reaction commentId:{}", commentId);
        Optional<String> username = ContextUtil.getUsername();
        if (username.isEmpty()) {
            return 0;
        }
        Optional<LikeComment> reaction = likeCommentRepository.findLikeCommentByUsernameAndId(username.get(), commentId);
        Integer r = reaction.map(LikeComment::getReaction).orElse(0);
        log.debug("username:{}, reaction: {}", username, r);
        return r;
    }
}

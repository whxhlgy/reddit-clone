package me.project.backend.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.exception.ServiceRuntimeException;
import me.project.backend.payload.dto.LikeDTO;
import me.project.backend.payload.request.LikeRequest;
import me.project.backend.service.IService.ILikeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class LikeServiceUseCache implements ILikeService {
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Boolean> redisScript;
    private final String POST_TYPE = "post";
    private final String COMMENT_TYPE = "comment";

    private String getLikeKey(long id, String type) {
        return "like:" + type + ":" + id;
    }
    private String getDislikeKey(long id, String type) {
        return "dislike:" + type + ":" + id;
    }

    public LikeServiceUseCache(StringRedisTemplate redisTemplate, RedisScript<Boolean> redisScript) {
        this.redisTemplate = redisTemplate;
        this.redisScript = redisScript;
    }

    public boolean updateReaction(String type, String id, String reaction, String username) {
        Object[] args = new Object[]{type, id, reaction, username};
        return redisTemplate.execute(redisScript, List.of(), args);
    }


    @Override
    public int getUserReactionByCommentId(String username, long commentId) {
        return getUserReactionByIdAndType(username, commentId, COMMENT_TYPE);
    }

    @Override
    public int getUserReactionByPostId(String username, long postId) {
        return getUserReactionByIdAndType(username, postId, POST_TYPE);
    }

    private int getUserReactionByIdAndType(String username, long postId, String postType) {
        String likeKey = getLikeKey(postId, postType);
        String dislikeKey = getDislikeKey(postId, postType);
        boolean isLike = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeKey, username));
        boolean isDislike = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(dislikeKey, username));
        return isLike ? 1 : isDislike ? -1 : 0;
    }

    @Override
    public LikeDTO likeCommentById(long commentId, @Valid LikeRequest likeRequest) {
        log.debug("like Comment By id: {}, request body: {}", commentId, likeRequest);
        String username = likeRequest.getUsername();
        int reaction = likeRequest.getReaction();
        if (updateReaction(COMMENT_TYPE, String.valueOf(commentId), String.valueOf(reaction), username)) {
            return new LikeDTO(username, reaction, LikeDTO.Type.COMMENT);
        } else {
            throw new ServiceRuntimeException("Like comment error");
        }
    }

    @Override
    public LikeDTO likePostById(long postId, LikeRequest likeRequest) {
        log.debug("like Post By id: {}, request body: {}", postId, likeRequest);
        String username = likeRequest.getUsername();
        int reaction = likeRequest.getReaction();
        if (updateReaction(POST_TYPE, String.valueOf(postId), String.valueOf(reaction), username)) {
            return new LikeDTO(username, reaction, LikeDTO.Type.POST);
        } else {
            throw new ServiceRuntimeException("Like comment error");
        }
    }

    @Override
    public long countLikeByCommentId(long commentId) {
        Long likeCount = Objects.requireNonNullElse(redisTemplate.opsForSet().size(getLikeKey(commentId, COMMENT_TYPE)), 0L);
        Long dislikeCount = Objects.requireNonNullElse(redisTemplate.opsForSet().size(getDislikeKey(commentId, COMMENT_TYPE)), 0L);
        return likeCount - dislikeCount;
    }

    @Override
    public long countLikeByPostId(long postId) {
        log.debug("count like by postId: {}", postId);
        Long likeCount = Objects.requireNonNullElse(redisTemplate.opsForSet().size(getLikeKey(postId, POST_TYPE)), 0L);
        Long dislikeCount = Objects.requireNonNullElse(redisTemplate.opsForSet().size(getDislikeKey(postId, POST_TYPE)), 0L);
        log.debug("like count: key {} -> {}, dislike count: key {} -> {}", getLikeKey(postId, POST_TYPE), likeCount, getDislikeKey(postId, POST_TYPE), dislikeCount);
        return likeCount - dislikeCount;
    }
}

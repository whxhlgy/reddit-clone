package me.project.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.domain.Post;
import me.project.backend.domain.Subscription;
import me.project.backend.domain.User;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.repository.PostRepository;
import me.project.backend.repository.SubscriptionRepository;
import me.project.backend.service.IService.ILikeService;
import me.project.backend.util.RedisKeys;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeedService {
    private final SubscriptionRepository subscriptionRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final PostRepository postRepository;
    private final ILikeService likeService;
    private final ModelMapper mapper;
    private final PostCacheService postCacheService;
    private final RedisScript<Boolean> fetchNewsScript;
    private final int FAN_OUT_MAX = 2;

    public FeedService(SubscriptionRepository subscriptionRepository, StringRedisTemplate stringRedisTemplate, PostRepository postRepository, ILikeService likeService, ModelMapper mapper, PostCacheService postCacheService,
                       @Qualifier("fetchNewsScript") RedisScript<Boolean> fetchNewsScript) {
        this.subscriptionRepository = subscriptionRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.postRepository = postRepository;
        this.likeService = likeService;
        this.mapper = mapper;
        this.postCacheService = postCacheService;
        this.fetchNewsScript = fetchNewsScript;
    }

    public List<PostDTO> getUserTimeLine(String username, int page, int size) {
        int start = page * size;
        int end = start + size - 1;

        List<Community> communities = subscriptionRepository.findAllWithCommunityByUserId(username).stream().map(Subscription::getCommunity).toList();
        for (Community community : communities) {
            if (community.getFollowerCount() > FAN_OUT_MAX) {
                log.debug("find popular community: {}", community.getName());
                String commKey = RedisKeys.getCommTimelineKey(community.getName());
                String userKey = RedisKeys.getTimelineKey(username);
                stringRedisTemplate.execute(fetchNewsScript, List.of(commKey, userKey));
            }
        }

        Set<String> idStrings = stringRedisTemplate.opsForZSet().reverseRange(RedisKeys.getTimelineKey(username), start, end);
        if (idStrings == null || idStrings.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = idStrings.stream().map(Long::parseLong).toList();
        List<Post> posts = postRepository.findAllInIds(ids);
        Collections.reverse(posts);
        return posts.stream().map(post -> convertPostToDTOWithReactionAndLikeCount(username, post)).collect(Collectors.toList());
    }

    public void savePostForFeed(Community community, long postId) {
        if (community.getFollowerCount() > FAN_OUT_MAX) {
            log.debug("find popular community: {}, saved to itself timeline", community.getName());
            savePostInCommTimeline(community, postId);
            return;
        }
        // find all users who subscribed this community
        List<Subscription> subs = subscriptionRepository.findAllByCommunityId(community.getId());
        List<User> users = subs.stream().map(Subscription::getUser).toList();
        users.forEach((user -> {
            savePostInUserTimeline(user.getUsername(), postId);
        }));
    }
    public void savePostInUserTimeline(String username, long postId) {
        stringRedisTemplate.opsForZSet().add(RedisKeys.getTimelineKey(username), String.valueOf(postId), System.currentTimeMillis());
    }

    public void savePostInCommTimeline(Community community, long postId) {
        stringRedisTemplate.opsForZSet().add(RedisKeys.getCommTimelineKey(community.getName()), String.valueOf(postId), System.currentTimeMillis());
    }

    private PostDTO convertPostToDTOWithReactionAndLikeCount(String username, Post post) {
        return getPostDTO(username, post, mapper, likeService, postCacheService);
    }

    static PostDTO getPostDTO(String username, Post post, ModelMapper mapper, ILikeService likeService, PostCacheService postCacheService) {
        PostDTO dto = mapper.map(post, PostDTO.class);
        dto.setReaction(likeService.getUserReactionByPostId(username, post.getId()));
        dto.setLikeCount(likeService.countLikeByPostId(post.getId()));
        dto.setUsername(post.getUser().getUsername());
        dto.setViewCount(postCacheService.getViewCount(post.getId()));
        return dto;
    }
}

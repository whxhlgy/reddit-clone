package me.project.backend.service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
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
    private final int FAN_OUT_MAX;
    private final String STREAM_KEY;
    private final String GROUP_NAME;
    private final String CUSTOMER_NAME;
    private final String POST_ID_FIELD;
    private final String POST_COMM_FIELD;
    private final int CONSUME_TIME_OUT;
    private final long UPDATE_TIMELINE_INTERVAL = 1000 * 60;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        try {
            stringRedisTemplate.opsForStream().createGroup(STREAM_KEY, GROUP_NAME);
        } catch (InvalidDataAccessApiUsageException e) {
            log.debug("{}", e.getMessage());
        }
        Thread taskFetcher = new Thread(() -> {
            while (true) {
                StreamOffset<String> streamOffset = StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed());
                List<MapRecord<String, Object, Object>> records = stringRedisTemplate.opsForStream().read(
                        Consumer.from(GROUP_NAME, CUSTOMER_NAME),
                        StreamReadOptions.empty().count(1).block(Duration.of(CONSUME_TIME_OUT, ChronoUnit.MILLIS)),
                        streamOffset);
                if (records == null || records.size() == 0) {
                    continue;
                }
                MapRecord<String, Object, Object> record = records.get(0);
                Long postId = Long.valueOf((String) record.getValue().get(POST_ID_FIELD));
                Long communityId = Long.valueOf((String) record.getValue().get(POST_COMM_FIELD));
                List<Subscription> subs = subscriptionRepository.findAllByCommunityId(communityId);
                List<User> users = subs.stream().map(Subscription::getUser).toList();
                users.forEach((user -> {
                    savePostInUserTimeline(user.getUsername(), postId);
                }));
                stringRedisTemplate.opsForStream().acknowledge(GROUP_NAME, record);
                log.debug("opeartion success, postId: {}, communityId: {}", postId, communityId);
            }
        });
        taskFetcher.setDaemon(true);
        taskFetcher.start();
    }

    public FeedService(SubscriptionRepository subscriptionRepository, StringRedisTemplate stringRedisTemplate,
            PostRepository postRepository, ILikeService likeService, ModelMapper mapper,
            PostCacheService postCacheService,
            TaskExecutor taskExecutor,
            @Qualifier("fetchNewsScript") RedisScript<Boolean> fetchNewsScript) {
        this.FAN_OUT_MAX = 2;
        this.STREAM_KEY = "stream:post2save";
        this.GROUP_NAME = "threads";
        this.CUSTOMER_NAME = "thread1";
        this.subscriptionRepository = subscriptionRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.postRepository = postRepository;
        this.likeService = likeService;
        this.mapper = mapper;
        this.postCacheService = postCacheService;
        this.fetchNewsScript = fetchNewsScript;
        this.POST_ID_FIELD = "id";
        this.POST_COMM_FIELD = "communityId";
        this.CONSUME_TIME_OUT = 1000;

    }

    public List<PostDTO> getUserTimeLine(String username, int page, int size) {
        int start = page * size;
        int end = start + size - 1;

        String lastUpdated = stringRedisTemplate.opsForValue().get(RedisKeys.getTimeLineLastUpdated(username));
        if (lastUpdated == null || System.currentTimeMillis() - Long.valueOf(lastUpdated) > UPDATE_TIMELINE_INTERVAL) {

            List<Community> communities = subscriptionRepository.findAllWithCommunityByUserId(username).stream()
                    .map(Subscription::getCommunity).toList();
            for (Community community : communities) {
                if (community.getFollowerCount() > FAN_OUT_MAX) {
                    log.debug("find popular community: {}", community.getName());
                    String commKey = RedisKeys.getCommTimelineKey(community.getName());
                    String userKey = RedisKeys.getTimelineKey(username);
                    stringRedisTemplate.execute(fetchNewsScript, List.of(commKey, userKey));
                }
            }

            stringRedisTemplate.opsForValue().set(RedisKeys.getTimeLineLastUpdated(username),
                    String.valueOf(System.currentTimeMillis()));
        }

        Set<String> idStrings = stringRedisTemplate.opsForZSet().reverseRange(RedisKeys.getTimelineKey(username), start,
                end);
        if (idStrings == null || idStrings.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = idStrings.stream().map(Long::parseLong).toList();
        List<Post> posts = postRepository.findAllInIds(ids);
        Collections.reverse(posts);
        return posts.stream().map(post -> convertPostToDTOWithReactionAndLikeCount(username, post))
                .collect(Collectors.toList());
    }

    public void savePostForFeed(Community community, long postId) {
        if (community.getFollowerCount() > FAN_OUT_MAX) {
            log.debug("find popular community: {}, saved to itself timeline", community.getName());
            savePostInCommTimeline(community, postId);
            return;
        }
        // slow operation, do it asynchroniously
        Map<String, String> fields = Map.of(
                POST_ID_FIELD, String.valueOf(postId),
                POST_COMM_FIELD, String.valueOf(community.getId()));
        MapRecord<String, String, String> mapBacked = StreamRecords.mapBacked(fields).withStreamKey(STREAM_KEY);
        stringRedisTemplate.opsForStream().add(mapBacked);
    }

    public void savePostInUserTimeline(String username, long postId) {
        log.debug("save a post: {} in {} timeline", postId, username);
        stringRedisTemplate.opsForZSet().add(RedisKeys.getTimelineKey(username), String.valueOf(postId),
                System.currentTimeMillis());
    }

    public void savePostInCommTimeline(Community community, long postId) {
        stringRedisTemplate.opsForZSet().add(RedisKeys.getCommTimelineKey(community.getName()), String.valueOf(postId),
                System.currentTimeMillis());
    }

    private PostDTO convertPostToDTOWithReactionAndLikeCount(String username, Post post) {
        return getPostDTO(username, post, mapper, likeService, postCacheService);
    }

    static PostDTO getPostDTO(String username, Post post, ModelMapper mapper, ILikeService likeService,
            PostCacheService postCacheService) {
        PostDTO dto = mapper.map(post, PostDTO.class);
        dto.setReaction(likeService.getUserReactionByPostId(username, post.getId()));
        dto.setLikeCount(likeService.countLikeByPostId(post.getId()));
        dto.setUsername(post.getUser().getUsername());
        dto.setViewCount(postCacheService.getViewCount(post.getId()));
        return dto;
    }

}

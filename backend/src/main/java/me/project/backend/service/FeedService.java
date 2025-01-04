package me.project.backend.service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.domain.Feed;
import me.project.backend.domain.Post;
import me.project.backend.domain.Subscription;
import me.project.backend.domain.User;
import me.project.backend.exception.notFound.PostNotFoundException;
import me.project.backend.exception.notFound.UserNotFoundException;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.payload.response.PaginatedResponse;
import me.project.backend.repository.FeedRepository;
import me.project.backend.repository.PostRepository;
import me.project.backend.repository.SubscriptionRepository;
import me.project.backend.repository.UserRepository;
import me.project.backend.service.IService.ILikeService;
import me.project.backend.util.RedisKeys;

@Service
@Slf4j
public class FeedService {
    private final SubscriptionRepository subscriptionRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
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
    private final TransactionHandler transactionHandler;

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
                // get the task with postId and communityId
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
                log.debug("get task of save post for feed, postId: {}, communityId: {}", postId, communityId);

                transactionHandler.runInTransaction(() -> {
                    log.debug("should start a new session: {}", TransactionAspectSupport.currentTransactionStatus());
                    Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
                    List<Subscription> subs = subscriptionRepository.findAllByCommunityId(communityId);
                    List<User> users = subs.stream().map(Subscription::getUser).toList();
                    users.forEach((user -> {
                        Long userId = user.getId();
                        log.debug("save a post: {} in {} feed", post.getId(), userId);
                        Feed feed = feedRepository.findByUserId(userId);
                        feed.addPost(post);
                    }));
                    return true;
                });

                // start task(intensive)
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
            @Qualifier("fetchNewsScript") RedisScript<Boolean> fetchNewsScript,
            UserRepository userRepository,
            FeedRepository feedRepository, TransactionHandler transactionHandler) {
        this.FAN_OUT_MAX = 0;
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
        this.feedRepository = feedRepository;
        this.transactionHandler = transactionHandler;
        this.userRepository = userRepository;
    }

    @Transactional
    public PaginatedResponse<PostDTO> getUserTimeLine(String username, int page, int size) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        // very time-consuming operation, limit its frequency
        String lastUpdatedString = stringRedisTemplate.opsForValue().get(RedisKeys.getTimeLineLastUpdated(username));
        if (lastUpdatedString == null
                || Instant.now().isAfter(Instant.parse(lastUpdatedString).plus(1, ChronoUnit.MINUTES))) {
            List<Community> communities = subscriptionRepository.findAllWithCommunityByUserId(username).stream()
                    .map(Subscription::getCommunity).toList();
            for (Community community : communities) {
                if (community.getFollowerCount() > FAN_OUT_MAX) {
                    log.debug("find popular community: {}, mannually load feed", community.getName());
                    fetchUserFeedByCommunity(user, community);
                }
            }
            stringRedisTemplate.opsForValue().set(RedisKeys.getTimeLineLastUpdated(username),
                    Instant.now().toString());
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> posts = feedRepository.findPostsByUsername(user.getUsername(), pageRequest);
        Page<PostDTO> postDTOs = posts.map(post -> convertPostToDTOWithReactionAndLikeCount(username, post));

        return new PaginatedResponse<>(postDTOs);
    }

    public void fetchUserFeedByCommunity(User user, Community community) {
        List<Post> posts = postRepository.findTop50ByCommunityNameOrderByCreatedAtDesc(community.getName());
        for (Post post : posts) {
            Feed feed = user.getFeed();
            feed.addPost(post);
        }
    }

    public void savePostForFeed(Community community, long postId) {
        if (community.getFollowerCount() > FAN_OUT_MAX) {
            log.debug("find popular community, use pull-strategy instead");
            return;
        }
        // slow operation, do it asynchroniously
        Map<String, String> fields = Map.of(
                POST_ID_FIELD, String.valueOf(postId),
                POST_COMM_FIELD, String.valueOf(community.getId()));
        MapRecord<String, String, String> mapBacked = StreamRecords.mapBacked(fields).withStreamKey(STREAM_KEY);
        stringRedisTemplate.opsForStream().add(mapBacked);
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

package me.project.backend.service;

import static me.project.backend.service.FeedService.getPostDTO;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.domain.Post;
import me.project.backend.domain.User;
import me.project.backend.exception.notFound.CommunityNotFoundException;
import me.project.backend.exception.notFound.PostNotFoundException;
import me.project.backend.exception.notFound.UserNotFoundException;
import me.project.backend.payload.PaginationInfo;
import me.project.backend.payload.UserDetailsImpl;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.payload.request.PostRequest;
import me.project.backend.payload.response.FindPostsResponse;
import me.project.backend.payload.response.PaginatedResponse;
import me.project.backend.repository.CommunityRepository;
import me.project.backend.repository.PostRepository;
import me.project.backend.repository.UserRepository;
import me.project.backend.service.IService.ILikeService;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper;
    private final ILikeService likeService;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final PostCacheService postCacheService;
    private final int POST_CACHE_THRESHOLD = 1;
    private final FeedService feedService;

    public PostService(PostRepository postRepository, ModelMapper mapper, ILikeService likeService,
            CommunityRepository communityRepository, UserRepository userRepository, PostCacheService postCacheService,
            FeedService feedService) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.likeService = likeService;
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.postCacheService = postCacheService;
        this.feedService = feedService;
    }

    public List<PostDTO> findAll() {
        List<Post> all = postRepository.findAll();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = principal.getUsername();
        return all.stream().map((post -> convertPostToDTOWithReactionAndLikeCount(username, post))).toList();
    }

    @Deprecated
    public List<Post> saveAll(List<Post> posts) {
        log.debug("bulk save");
        return postRepository.saveAll(posts);
    }

    public PostDTO saveByCommunityName(String name, PostRequest postRequest) {
        log.info("save post by comm name: {}", postRequest);
        Post post = mapper.map(postRequest, Post.class);
        Community community = communityRepository.findByName(name)
                .orElseThrow(() -> new CommunityNotFoundException(name));
        User user = userRepository.findByUsername(postRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException(postRequest.getUsername()));
        post.setCommunity(community);
        post.setUser(user);
        Post save = postRepository.save(post);
        feedService.savePostForFeed(community, post.getId());
        return mapper.map(save, PostDTO.class);
    }

    public PostDTO findById(long postId) {
        log.info("find post by id: {}", postId);
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = principal.getUsername();
        log.debug("try to get post by id from cache: {}", postId);
        PostDTO postDTO = postCacheService.getPost(postId);
        if (postDTO != null) {
            postDTO.setViewCount(postCacheService.getViewCount(postDTO.getId()));
            postDTO.setReaction(likeService.getUserReactionByPostId(username, postDTO.getId()));
            postDTO.setLikeCount(likeService.countLikeByPostId(postDTO.getId()));
            return postDTO;
        }
        log.debug("failed, retry get post by id from db: {}", postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));

        long viewCount = postCacheService.increaseViewCount(postId, username);
        if (viewCount >= POST_CACHE_THRESHOLD) {
            log.debug("The post(id: {}) view count exceeds the threshold of {}, try cache it", postId,
                    POST_CACHE_THRESHOLD);
            postCacheService.savePost(post);
        }
        return convertPostToDTOWithReactionAndLikeCount(username, post);
    }

    public PaginatedResponse<PostDTO> findAllByCommunityName(String name, int page, int size) {
        log.debug("find all posts by community name: {}, page: {}, size: {}", name, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByCommunityName(pageable, name);
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = principal.getUsername();

        Page<PostDTO> postDTOs = posts.map(post -> convertPostToDTOWithReactionAndLikeCount(username, post));
        return new PaginatedResponse<>(postDTOs);
    }

    private PostDTO convertPostToDTOWithReactionAndLikeCount(String username, Post post) {
        return getPostDTO(username, post, mapper, likeService, postCacheService);
    }

}

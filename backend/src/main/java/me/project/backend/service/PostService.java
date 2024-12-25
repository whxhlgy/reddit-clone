package me.project.backend.service;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.domain.Post;
import me.project.backend.domain.User;
import me.project.backend.exception.notFound.CommunityNotFoundException;
import me.project.backend.exception.notFound.PostNotFoundException;
import me.project.backend.exception.notFound.UserNotFoundException;
import me.project.backend.payload.UserDetailsImpl;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.payload.request.PostRequest;
import me.project.backend.repository.CommunityRepository;
import me.project.backend.repository.PostRepository;
import me.project.backend.repository.UserRepository;
import me.project.backend.service.IService.ILikeService;
import me.project.backend.util.RedisKeys;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper;
    private final ILikeService likeService;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public PostService(PostRepository postRepository, ModelMapper mapper, ILikeService likeService, CommunityRepository communityRepository, UserRepository userRepository, StringRedisTemplate stringRedisTemplate) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.likeService = likeService;
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public List<PostDTO> findAll() {
        List<Post> all = postRepository.findAll();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        return all.stream().map((post -> convertPostToDTOWithReactionAndLikeCount(username, post))).toList();
    }

    public PostDTO save(Post post) {
        log.info("save post: {}", post);
        Post save = postRepository.save(post);
        return mapper.map(post, PostDTO.class);
    }

    public List<Post> saveAll(List<Post> posts) {
        log.debug("bulk save");
        return postRepository.saveAll(posts);
    }

    public PostDTO saveByCommunityName(String name, PostRequest postRequest) {
        log.info("save post by comm name: {}", postRequest);
        Post post = mapper.map(postRequest, Post.class);
        Community community = communityRepository.findByName(name).orElseThrow(() -> new CommunityNotFoundException(name));
        User user = userRepository.findByUsername(postRequest.getUsername()).orElseThrow(() -> new UserNotFoundException(postRequest.getUsername()));
        post.setCommunity(community);
        post.setUser(user);
        Post save = postRepository.save(post);
        return mapper.map(save, PostDTO.class);
    }

    public PostDTO findById(long postId) {
        log.info("find post by id: {}", postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        increaseViewCount(postId, username);
        return convertPostToDTOWithReactionAndLikeCount(username, post);
    }

    public List<PostDTO> findAllByCommunityName(String name, int page, int size) {
        log.debug("find all posts by community name: {}, page: {}, size: {}", name, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByCommunityName(pageable, name);
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        return posts.stream().map(post -> convertPostToDTOWithReactionAndLikeCount(username, post)).collect(Collectors.toList());
    }

    private PostDTO convertPostToDTOWithReactionAndLikeCount(String username, Post post) {
        PostDTO dto = mapper.map(post, PostDTO.class);
        dto.setReaction(likeService.getUserReactionByPostId(username, post.getId()));
        dto.setLikeCount(likeService.countLikeByPostId(post.getId()));
        dto.setUsername(post.getUser().getUsername());
        dto.setViewCount(getViewCount(post.getId()));
        return dto;
    }

    private void increaseViewCount(long postId, String username) {
        String key = RedisKeys.getPostViewKey(postId);
        stringRedisTemplate.opsForHyperLogLog().add(key, username);
    }
    private long getViewCount(long postId) {
        String key = RedisKeys.getPostViewKey(postId);
        return stringRedisTemplate.opsForHyperLogLog().size(key);
    }
}

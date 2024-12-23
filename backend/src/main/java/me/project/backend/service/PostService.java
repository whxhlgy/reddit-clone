package me.project.backend.service;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Community;
import me.project.backend.domain.Post;
import me.project.backend.exception.notFound.CommunityNotFoundException;
import me.project.backend.exception.notFound.PostNotFoundException;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.payload.request.PostRequest;
import me.project.backend.repository.CommunityRepository;
import me.project.backend.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper;
    private final LikeService likeService;
    private final CommunityRepository communityRepository;

    public PostService(PostRepository postRepository, ModelMapper mapper, LikeService likeService, CommunityRepository communityRepository) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.likeService = likeService;
        this.communityRepository = communityRepository;
    }

    public List<PostDTO> findAll() {
        List<Post> all = postRepository.findAll();
        return all.stream().map(this::convertPostToDTOWithReactionAndLikeCount).toList();
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
        Post map = mapper.map(postRequest, Post.class);
        Community community = communityRepository.findByName(name).orElseThrow(() -> new CommunityNotFoundException(name));
        map.setCommunity(community);
        Post save = postRepository.save(map);
        return mapper.map(save, PostDTO.class);
    }

    public PostDTO findById(long postId) {
        log.info("find post by id: {}", postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return convertPostToDTOWithReactionAndLikeCount(post);
    }

    public List<PostDTO> findAllByCommunityName(String name) {
        log.debug("find all posts by community name: {}", name);
        List<PostDTO> posts = postRepository.findPostByCommunityName(name);
        return posts.stream().peek((postDTO -> {
            postDTO.setReaction(likeService.getUserReactionByPostId(postDTO.getId()));
            postDTO.setLikeCount(likeService.countLikeByPostId(postDTO.getId()));
        })).collect(Collectors.toList());
    }

    private PostDTO convertPostToDTOWithReactionAndLikeCount(Post post) {
        PostDTO dto = mapper.map(post, PostDTO.class);
        dto.setReaction(likeService.getUserReactionByPostId(post.getId()));
        dto.setLikeCount(likeService.countLikeByPostId(post.getId()));
        return dto;
    }
}

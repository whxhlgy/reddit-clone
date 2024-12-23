package me.project.backend.service;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Post;
import me.project.backend.exception.notFound.PostNotFoundException;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper;
    private final LikeService likeService;

    public PostService(PostRepository postRepository, ModelMapper mapper, LikeService likeService) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.likeService = likeService;
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

    public PostDTO findById(long postId) {
        log.info("find post by id: {}", postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return convertPostToDTOWithReactionAndLikeCount(post);
    }
    private PostDTO convertPostToDTOWithReactionAndLikeCount(Post post) {
        PostDTO dto = mapper.map(post, PostDTO.class);
        dto.setReaction(likeService.getUserReactionByPostId(post.getId()));
        dto.setLikeCount(likeService.countLikeByPostId(post.getId()));
        return dto;
    }
}

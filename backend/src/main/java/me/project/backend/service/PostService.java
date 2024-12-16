package me.project.backend.service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Post;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper mapper;

    public PostService(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    public List<PostDTO> findAll() {
        List<Post> all = postRepository.findAll();
        return mapper.map(all, new TypeToken<List<PostDTO>>() {}.getType());
    }

    public PostDTO save(Post post) {
        log.info("save post: {}", post);
        Post save = postRepository.save(post);
        return mapper.map(post, PostDTO.class);
    }
}

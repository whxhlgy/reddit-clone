package me.project.backend.service;

import lombok.extern.java.Log;
import me.project.backend.domain.Post;
import me.project.backend.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        log.info("save post: " + post);
        return postRepository.save(post);
    }
}

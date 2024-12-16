package me.project.backend.controller;

import me.project.backend.domain.Post;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDTO> findAll() {
        return postService.findAll();
    }

    @GetMapping("/{postId}")
    public PostDTO findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @PostMapping
    public PostDTO save(@RequestBody Post post) {
        return postService.save(post);
    }
}

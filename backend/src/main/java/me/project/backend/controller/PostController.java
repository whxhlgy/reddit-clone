package me.project.backend.controller;

import me.project.backend.domain.Post;
import me.project.backend.payload.dto.PostDTO;
import me.project.backend.payload.request.PostRequest;
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

    @GetMapping("/community/{name}")
    public List<PostDTO> findAllByCommunity(@PathVariable String name) {
        return postService.findAllByCommunityName(name);
    }

    @GetMapping("/{postId}")
    public PostDTO findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @PostMapping("/community/{name}")
    public PostDTO saveByCommunityName(@PathVariable String name, @RequestBody PostRequest postRequest) {
        return postService.saveByCommunityName(name, postRequest);
    }

    @PostMapping
    public PostDTO save(@RequestBody Post post) {
        return postService.save(post);
    }
}

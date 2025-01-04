package me.project.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.project.backend.payload.dto.PostDTO;
import me.project.backend.payload.request.PostRequest;
import me.project.backend.payload.response.PaginatedResponse;
import me.project.backend.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // DECRYPTED
    @GetMapping
    public List<PostDTO> findAll() {
        return postService.findAll();
    }

    @GetMapping("/community/{name}")
    public PaginatedResponse<PostDTO> findAllByCommunity(@PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return postService.findAllByCommunityName(name, page, size);
    }

    @GetMapping("/{postId}")
    public PostDTO findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @PostMapping("/community/{name}")
    public PostDTO saveByCommunityName(@PathVariable String name, @RequestBody PostRequest postRequest) {
        return postService.saveByCommunityName(name, postRequest);
    }
}

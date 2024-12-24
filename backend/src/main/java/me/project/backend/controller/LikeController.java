package me.project.backend.controller;

import me.project.backend.payload.dto.LikeDTO;
import me.project.backend.payload.request.LikeRequest;
import me.project.backend.service.IService.ILikeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    private final ILikeService likeService;

    public LikeController(ILikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/comment/{id}")
    public LikeDTO likeComment(@PathVariable Long id, @RequestBody LikeRequest likeRequest) {
        return likeService.likeCommentById(id, likeRequest);
    }

    @PostMapping("/post/{id}")
    public LikeDTO likePost(@PathVariable Long id, @RequestBody LikeRequest likeRequest) {
        return likeService.likePostById(id, likeRequest);
    }

}

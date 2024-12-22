package me.project.backend.controller;

import me.project.backend.payload.dto.LikeDTO;
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
    public LikeDTO likeComment(@PathVariable Long id, @RequestParam Integer reaction) {
        return likeService.likeCommentById(id, reaction);
    }

    @PostMapping("/post/{id}")
    public LikeDTO likePost(@PathVariable Long id, @RequestParam Integer reaction) {
        return likeService.likePostById(id, reaction);
    }

}

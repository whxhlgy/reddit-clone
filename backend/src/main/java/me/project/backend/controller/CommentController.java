package me.project.backend.controller;

import me.project.backend.payload.dto.CommentDTO;
import me.project.backend.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{postId}")
    public List<CommentDTO> findCommentsByPostId(@PathVariable int postId){
        return commentService.findAll(postId);
    }
}

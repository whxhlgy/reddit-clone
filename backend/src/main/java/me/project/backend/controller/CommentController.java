package me.project.backend.controller;

import jakarta.validation.Valid;
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
    public List<CommentDTO> findCommentsByPostId(@PathVariable int postId) {
        return commentService.findAll(postId);
    }

    @PostMapping("/{postId}")
    public CommentDTO saveComment(@PathVariable long postId, @RequestBody @Valid CommentDTO commentDTO) {
        return commentService.save(postId, commentDTO);
    }
}

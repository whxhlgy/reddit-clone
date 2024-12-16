package me.project.backend.payload.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import me.project.backend.domain.Post;

@Data
public class CommentDTO {
    private Long id;

    @NotBlank
    private String content;

    private Long parentCommentId;

    @NotBlank
    private String username;
}

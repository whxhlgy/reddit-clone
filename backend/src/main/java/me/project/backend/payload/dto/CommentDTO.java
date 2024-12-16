package me.project.backend.payload.dto;

import lombok.Data;
import me.project.backend.domain.Post;

@Data
public class CommentDTO {
    private Long id;

    private String content;

    private Long parentCommentId;
}

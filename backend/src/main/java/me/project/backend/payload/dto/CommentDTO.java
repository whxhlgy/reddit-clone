package me.project.backend.payload.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.project.backend.domain.Post;

@Data
@NoArgsConstructor
public class CommentDTO {
    private Long id;

    @NotBlank
    private String content;

    @NotBlank
    private String username;

    public CommentDTO(String content, String username) {
        this.content = content;
        this.username = username;
    }

}

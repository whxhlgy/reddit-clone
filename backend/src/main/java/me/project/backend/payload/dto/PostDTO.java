package me.project.backend.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.project.backend.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;

    private String username;

    private Long communityId;

    private String title;

    private String content;

    private int reaction;

    private int likeCount;
}

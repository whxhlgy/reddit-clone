package me.project.backend.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.project.backend.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long id;

    private String username;

    private Long communityId;

    private String title;

    private String content;

    private int reaction;

    private long likeCount;

    private long viewCount;
}

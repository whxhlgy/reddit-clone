package me.project.backend.payload.request;

import lombok.Data;
import me.project.backend.domain.Community;

@Data
public class PostRequest {
    private String username;
    private String title;
    private String content;
}

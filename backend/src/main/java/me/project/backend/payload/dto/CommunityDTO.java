package me.project.backend.payload.dto;

import lombok.Data;

@Data
public class CommunityDTO {
    private Long id;
    private String name;
    private String description;
    private long followerCount;
}

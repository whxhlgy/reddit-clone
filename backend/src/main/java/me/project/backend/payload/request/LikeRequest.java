package me.project.backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LikeRequest {
    @NotBlank
    private int reaction;
    @NotBlank
    private String username;
}

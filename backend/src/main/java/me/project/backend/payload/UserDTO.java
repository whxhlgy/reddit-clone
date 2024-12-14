package me.project.backend.payload;

import lombok.Builder;

@Builder
public record UserDTO(String username) {
}

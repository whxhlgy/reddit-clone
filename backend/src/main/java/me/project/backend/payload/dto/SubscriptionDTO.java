package me.project.backend.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.project.backend.domain.Community;
import me.project.backend.domain.User;

@Data
@AllArgsConstructor
public class SubscriptionDTO {
    private Long id;
    private String username;
    private String communityName;
}

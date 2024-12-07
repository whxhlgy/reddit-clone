package me.project.backend.payload.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank
    @JsonAlias("refresh_token")
    private String refreshToken;
}

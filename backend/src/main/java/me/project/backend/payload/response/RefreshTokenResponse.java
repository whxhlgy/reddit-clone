package me.project.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RefreshTokenResponse {
    String message = "Refresh token successfully";
}

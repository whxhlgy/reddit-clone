package me.project.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonIgnore
    private String refreshToken;
}

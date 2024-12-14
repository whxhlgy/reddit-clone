package me.project.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LoginResponse {
    private String message = "Login Successful";
    @JsonIgnore
    private String accessToken;

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @JsonIgnore
    private String refreshToken;
}

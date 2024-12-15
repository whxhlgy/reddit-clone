package me.project.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record SignOutResponse(@JsonIgnore String accessToken, @JsonIgnore String refreshToken, String message) {

}
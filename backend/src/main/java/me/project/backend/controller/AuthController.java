package me.project.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.payload.request.RefreshTokenRequest;
import me.project.backend.payload.response.JwtResponse;
import me.project.backend.payload.request.LoginRequest;
import me.project.backend.payload.request.SignupRequest;
import me.project.backend.payload.response.RefreshTokenResponse;
import me.project.backend.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest loginRequest){
        log.debug("login request: {}", loginRequest.toString());
        return authService.login(loginRequest);
    }

    @PostMapping("/signup")
    public JwtResponse signUp(@RequestBody SignupRequest signupRequest) {
        log.debug("signup request: {}", signupRequest.toString());
        return authService.signup(signupRequest);
    }

    @PostMapping("/refreshtoken")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

}

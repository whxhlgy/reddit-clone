package me.project.backend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private static void addTokenInCookie(HttpServletResponse response, JwtResponse login) {
        Cookie cookie = new Cookie("refresh_token", login.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        log.debug("login request: {}", loginRequest.toString());
        JwtResponse login = authService.login(loginRequest);
        addTokenInCookie(response, login);
        return login;
    }

    @PostMapping("/signup")
    public JwtResponse signUp(@RequestBody SignupRequest signupRequest, HttpServletResponse response) {
        log.debug("signup request: {}", signupRequest.toString());
        JwtResponse signup = authService.signup(signupRequest);
        addTokenInCookie(response, signup);
        return signup;
    }

    @PostMapping("/refreshtoken")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

}

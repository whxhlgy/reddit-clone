package me.project.backend.controller;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.payload.response.JwtResponse;
import me.project.backend.payload.response.request.LoginRequest;
import me.project.backend.payload.response.request.SignupRequest;
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
    public JwtResponse login(@RequestBody SignupRequest signupRequest) {
        log.debug("signup request: {}", signupRequest.toString());
        return authService.signup(signupRequest);
    }

}

package me.project.backend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.exception.auth.MissingRefreshTokenException;
import me.project.backend.payload.response.LoginResponse;
import me.project.backend.payload.request.LoginRequest;
import me.project.backend.payload.request.SignupRequest;
import me.project.backend.payload.response.RefreshTokenResponse;
import me.project.backend.payload.response.SignOutResponse;
import me.project.backend.service.AuthService;
import me.project.backend.util.Cookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final int jwtRefreshExpirationSc;
    private final int jwtExpirationSc;

    public AuthController(AuthService authService,
                          @Value("${junjiezh.app.jwtRefreshExpirationMs}") int jwtRefreshExpirationMs,
                          @Value("${junjiezh.app.jwtExpirationMs}") int jwtExpiration) {
        this.authService = authService;
        jwtRefreshExpirationSc = jwtRefreshExpirationMs / 1000;
        this.jwtExpirationSc = jwtExpiration / 1000;
    }

    private void addTokenInCookie(HttpServletResponse response, String accessToken, String refreshToken) {

        if (accessToken != null) {
            Cookie accessTokenCookie = new Cookie("access_token", accessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setMaxAge(jwtExpirationSc);
            accessTokenCookie.setSecure(false);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setAttribute("SameSite", "Strict");
            response.addCookie(accessTokenCookie);
        }

        if (refreshToken != null) {
            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setMaxAge(jwtRefreshExpirationSc);
            refreshTokenCookie.setSecure(false);
            refreshTokenCookie.setPath("/api/auth/refresh");
            refreshTokenCookie.setAttribute("SameSite", "Strict");
            response.addCookie(refreshTokenCookie);
        }

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        log.debug("login request: {}", loginRequest.toString());
        LoginResponse login = authService.login(loginRequest);
        addTokenInCookie(response, login.getAccessToken(), login.getRefreshToken());
        return login;
    }

    @PostMapping("/signup")
    public LoginResponse signUp(@RequestBody SignupRequest signupRequest, HttpServletResponse response) {
        log.debug("signup request: {}", signupRequest.toString());
        LoginResponse signup = authService.signup(signupRequest);
        addTokenInCookie(response, signup.getAccessToken(), signup.getRefreshToken());
        return signup;
    }

    @PostMapping("/signout")
    public SignOutResponse signOut(HttpServletResponse response) {
        SignOutResponse signOutResponse = authService.signOut();
        addTokenInCookie(response, signOutResponse.accessToken(), signOutResponse.refreshToken());
        return signOutResponse;
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshToken = Cookies.getCookie(request, "refresh_token");
        String token = authService.refreshToken(refreshToken.orElseThrow(()-> new MissingRefreshTokenException("Missing refresh token")));
        addTokenInCookie(response, token, null);
        return new RefreshTokenResponse();
    }

}

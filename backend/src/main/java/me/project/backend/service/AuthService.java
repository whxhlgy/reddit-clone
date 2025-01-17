package me.project.backend.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.Feed;
import me.project.backend.domain.User;
import me.project.backend.exception.auth.BadJwtTokenException;
import me.project.backend.exception.auth.InvalidCredentialsException;
import me.project.backend.exception.auth.UserAlreadyExistsException;
import me.project.backend.payload.UserDetailsImpl;
import me.project.backend.payload.response.LoginResponse;
import me.project.backend.payload.request.LoginRequest;
import me.project.backend.payload.request.SignupRequest;
import me.project.backend.payload.response.SignOutResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public LoginResponse login(@Valid LoginRequest loginRequest) {
        log.debug("User login: {}", loginRequest.getUsername());
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }
        // generate jwt token for this user
        log.debug("Get authentication success: {}", authentication);
        String jwtToken = jwtService.generateJwtToken(authentication);
        String jwtRefreshToken = jwtService.generateRefreshToken(authentication);
        return new LoginResponse(jwtToken, jwtRefreshToken);
    }

    /**
     * 1. check if the user already exist
     * 2. save the user
     * 3. reuse the login method
     * 
     * @param signupRequest
     * @return
     */
    public LoginResponse signup(@Valid SignupRequest signupRequest) {
        try {
            Feed feed = new Feed();
            User user = User.builder().username(signupRequest.getUsername()).password(signupRequest.getPassword())
                    .build();
            user.setFeed(feed);
            userService.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("The username: " + signupRequest.getUsername() + " already exists");
        }

        return login(new LoginRequest(signupRequest.getUsername(), signupRequest.getPassword()));
    }

    public String refreshToken(String refreshToken) {
        log.debug("Refresh token: {}", refreshToken);

        // validate the token, if the token is invalid, this will throw an exception
        if (!jwtService.validateToken(refreshToken)) {
            throw new BadJwtTokenException(refreshToken);
        }

        String username = jwtService.extractUsername(refreshToken);
        // this is a token with isAuthenticated()==true
        Authentication authentication = new UsernamePasswordAuthenticationToken(new UserDetailsImpl(username, null),
                null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateJwtToken(authentication);
    }

    /**
     * return two expired jwt token
     */
    public SignOutResponse signOut() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(new UserDetailsImpl("anonymous", null),
                null, List.of());
        String jwtToken = jwtService.generateJwtToken(authentication, Date.from(Instant.now()));
        return new SignOutResponse(jwtToken, jwtToken, "Sign out");
    }
}

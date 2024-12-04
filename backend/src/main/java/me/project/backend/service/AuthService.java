package me.project.backend.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.User;
import me.project.backend.exception.auth.UserAlreadyExistsException;
import me.project.backend.payload.response.JwtResponse;
import me.project.backend.payload.response.request.LoginRequest;
import me.project.backend.payload.response.request.SignupRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public JwtResponse login(@Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // generate jwt token for this user
        log.debug("Get authentication success: {}", authentication);
        String jwtToken = jwtService.generateJwtToken(authentication);
        return new JwtResponse(jwtToken);
    }

    /**
     * 1. check if the user already exist
     * 2. save the user
     * 3. reuse the login method
     * @param signupRequest
     * @return
     */
    public JwtResponse signup(@Valid SignupRequest signupRequest) {
        try {
            userService.save(new User(signupRequest.getUsername(), signupRequest.getPassword()));
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("The username: " + signupRequest.getUsername() + " already exists");
        }

        return login(new LoginRequest(signupRequest.getUsername(), signupRequest.getPassword()));
    }
}

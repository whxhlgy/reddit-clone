package me.project.backend.service;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;
    UserDetails userDetails = User.builder()
            .username("testUser")
            .password("password")
            .build();

    @BeforeEach
    void setUp() {
        String jwtSecret = "86c2b0a44ec2116a0e822584c7bad3e1357b6417bb81c7957c8e9301b580049c";
        int jwtExpirationMs = 3600000;
        int jwtRefreshTokenMs = 604800000;
        jwtService = new JwtService(jwtSecret, jwtExpirationMs, jwtRefreshTokenMs);
    }

    @Mock
    private Authentication authentication;

    @Test
    void generateTokenAndVerify() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String jwtToken = jwtService.generateJwtToken(authentication);
        assertThat(jwtToken).isNotNull();
        String username = jwtService.extractUsername(jwtToken);
        assertThat(username).isEqualTo("testUser");
    }

    @Test
    void verifyExpiredToken() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String jwtToken = jwtService.generateJwtToken(authentication, Date.from(Instant.now()));
        assertThat(jwtToken).isNotNull();
        assertThatThrownBy(() -> jwtService.extractUsername(jwtToken)).isInstanceOf(ExpiredJwtException.class);
    }

}
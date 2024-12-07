package me.project.backend.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import me.project.backend.domain.User;
import me.project.backend.exception.auth.RefreshTokenNotValidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    private final String jwtSecret;
    private final int jwtExpirationMs;
    private final int jwtRefreshExpirationMs;

    public JwtService(
            @Value("${junjiezh.app.jwtSecret}") String jwtSecret,
            @Value("${junjiezh.app.jwtExpirationMs}") int jwtExpirationMs,
            @Value("${junjiezh.app.jwtRefreshExpirationMs}") int jwtRefreshExpirationMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtRefreshExpirationMs = jwtRefreshExpirationMs;
    }

    private SecretKey secretKey;

    public SecretKey getSecretKey() {
        if (secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
        return secretKey;
    }

    public String generateJwtToken(Authentication authentication) {
        return generateJwtToken(authentication, Date.from(Instant.now().plusMillis(jwtExpirationMs)));
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateJwtToken(authentication, Date.from(Instant.now().plusMillis(jwtRefreshExpirationMs)));
    }

    public String generateJwtToken(Authentication authentication, Date expiration) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();


        String jwtToken = Jwts.builder()
                .subject(username)
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact();
        log.debug("Generated JWT Token: {}", jwtToken);
        return jwtToken;
    }

    public String extractUsername(String jwtToken) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwtToken);
        return claimsJws.getPayload().getSubject();
    }

    public void validateToken(String jwtToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parse(jwtToken);
        } catch (ExpiredJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) {
            throw new RefreshTokenNotValidException(e.getMessage());
        }
    }
}

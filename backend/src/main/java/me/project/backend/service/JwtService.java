package me.project.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
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

    public JwtService(
            @Value("${junjiezh.app.jwtSecret}") String jwtSecret,
            @Value("${junjiezh.app.jwtExpirationMs}")int jwtExpirationMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
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
}

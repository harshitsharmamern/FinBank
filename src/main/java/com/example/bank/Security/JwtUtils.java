package com.example.bank.Security;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private String jwtSecret = "your-very-long-secret-key-at-least-32-characters";
    private int jwtExpirationMs = 86400000; // 24 hours

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public String generateTokenForNewUser(String accountNumber) {
    return Jwts.builder()
            .subject(accountNumber) // Use Account Number here
            .issuedAt(new Date())
            .expiration(new Date((new Date()).getTime() + 86400000)) // 24 hours
            .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
            .compact();
}

    public String getUsernameFromToken(String token) {
        return Jwts.parser().
        verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
        .build().
        parseSignedClaims(token).
        getPayload().
        getSubject();
    }
}

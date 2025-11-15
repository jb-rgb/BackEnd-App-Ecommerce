package com.jorge.apirest.utils;

import com.jorge.apirest.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key = Keys.hmacShaKeyFor(
            "50b4b2f90cd04fb8b73bce8af5545d6bbfbe343f4a1f0222189483925277478e".getBytes(StandardCharsets.UTF_8)
    );

    public String generateToken(User user) {
        long expirationMillis = 1000 * 60 * 60 * 24;
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);
        return Jwts
                .builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}

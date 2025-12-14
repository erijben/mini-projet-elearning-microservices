package com.elearning.enrollment_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtils {

    private final Key key;

    public JwtUtils(@Value("${app.jwt.secret}") String jwtSecret) {
        if (jwtSecret == null || jwtSecret.trim().length() == 0) {
            throw new IllegalArgumentException("app.jwt.secret not set");
        }
        // use explicit UTF-8 to avoid encoding mismatch
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // retire "Bearer " si présent
    public static String tokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null) return null;
        String h = authorizationHeader.trim();
        if (h.toLowerCase().startsWith("bearer ")) {
            return h.substring(7).trim();
        }
        return h;
    }

    // parse et retourne Claims si valide, lance exception sinon
    public Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    // extrait userId (supposé stocké en subject)
    public Long extractUserId(String token) {
        Jws<Claims> jws = parseClaims(token);
        Claims claims = jws.getBody();
        String sub = claims.getSubject();
        if (sub == null) {
            Object possible = claims.get("userId");
            if (possible != null) return Long.valueOf(String.valueOf(possible));
            throw new IllegalStateException("user id not found in token");
        }
        return Long.valueOf(sub);
    }
}

package com.java.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;  // The secret key from application.properties

    // Generate a JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // Set the username as the subject
                .signWith(SignatureAlgorithm.HS256, jwtSecret)  // Use the secret key for signing
                .compact();
    }

    // Get username from the JWT token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)  // Use the same secret key for parsing
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();  // Return the username
    }

    // Validate the JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;  // Return false if the token is invalid
        }
    }
}
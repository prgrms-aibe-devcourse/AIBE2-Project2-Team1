package com.example.campy.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // AuthService용: userId 없이
    public String createToken(String username, String email, String role) {
        Date now = new Date();
        long expirationMs = 6 * 60 * 60 * 1000;
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    // TalentController용: userId 포함
    public String createToken(Integer userId, String username, String email, String role) {
        Date now = new Date();
        long expirationMs = 6 * 60 * 60 * 1000;
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        System.out.println("🔍 extractClaims 호출됨");
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        String username = extractClaims(token).getSubject();
        System.out.println("✅ getUsername: " + username);
        return username;
    }

    public String getEmail(String token) {
        String email = extractClaims(token).get("email", String.class);
        System.out.println("✅ getEmail: " + email);
        return email;
    }

    public String getRole(String token) {
        try {
            String role = extractClaims(token).get("role", String.class);
            System.out.println("✅ getRole: " + role);
            return role;
        } catch (Exception e) {
            System.out.println("❌ getRole 예외: " + e.getMessage());
            return null;
        }
    }

    public Integer getUserId(String token) {
        try {
            Integer userId = extractClaims(token).get("userId", Integer.class);
            System.out.println("✅ getUserId: " + userId);
            return userId;
        } catch (Exception e) {
            System.out.println("❌ getUserId 예외: " + e.getMessage());
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        boolean expired = expiration.before(new Date());
        System.out.println("⏰ isTokenExpired: " + expired);
        return expired;
    }

    public boolean validateToken(String token) {
        try {
            boolean expired = isTokenExpired(token);
            System.out.println("✅ validateToken - expired? " + expired);
            return !expired;
        } catch (Exception e) {
            System.out.println("❌ validateToken 예외: " + e.getMessage());
            return false;
        }
    }
}
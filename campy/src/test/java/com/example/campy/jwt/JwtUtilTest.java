package com.example.campy.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "NxD@49#jKw!rMv7Up2zLt$gQpY%1fA3eTb8CkD$HzLwRm";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret);
        System.out.println("✅ JwtUtil 인스턴스 생성 완료");
    }

    @Test
    void createAndValidateTokenTest() {
        try {
            String token = jwtUtil.createToken("junhyung", "jun@test.com", "USER");
            System.out.println("🔐 생성된 토큰: " + token);

            assertNotNull(token);
            assertEquals("junhyung", jwtUtil.getUsername(token));
            assertEquals("jun@test.com", jwtUtil.getEmail(token));
            assertEquals("USER", jwtUtil.getRole(token));
            assertTrue(jwtUtil.validateToken(token));

            System.out.println("✅ createAndValidateTokenTest 성공");
        } catch (Exception e) {
            System.out.println("❌ createAndValidateTokenTest 실패: " + e.getMessage());
            fail("예외 발생: " + e.getMessage());
        }
    }

    @Test
    void expiredTokenShouldBeInvalid() {
        try {
            String expiredToken = Jwts.builder()
                    .subject("expiredUser")
                    .claim("email", "old@test.com")
                    .claim("role", "USER")
                    .issuedAt(new Date(System.currentTimeMillis() - 10_000))
                    .expiration(new Date(System.currentTimeMillis() - 5_000))  // 이미 만료
                    .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .compact();

            System.out.println("🧨 만료된 토큰 생성 완료: " + expiredToken);

            assertFalse(jwtUtil.validateToken(expiredToken));

            System.out.println("✅ expiredTokenShouldBeInvalid 성공");
        } catch (Exception e) {
            System.out.println("❌ expiredTokenShouldBeInvalid 실패: " + e.getMessage());
            fail("예외 발생: " + e.getMessage());
        }
    }
}
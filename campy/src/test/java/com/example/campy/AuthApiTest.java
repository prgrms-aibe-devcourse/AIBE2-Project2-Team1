package com.example.campy;

import com.example.campy.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("NxD@49#jKw!rMv7Up2zLt$gQpY%1fA3eTb8CkD$HzLwRm");
        token = jwtUtil.createToken("junhyung", "jun@test.com", "USER");
        System.out.println("테스트용 JWT: " + token);
    }

    @Test
    void userEndpointWithTokenShouldSucceed() throws Exception {
        mockMvc.perform(get("/user/test") // 실제 경로 맞춰야 함
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void adminEndpointWithUserTokenShouldFail() throws Exception {
        mockMvc.perform(get("/admin/test") // 실제 경로 맞춰야 함
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void userEndpointWithoutTokenShouldFail() throws Exception {
        mockMvc.perform(get("/user/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        System.out.println("❌ 토큰 없이 요청 → 401 Unauthorized 확인 완료");
    }

    @Test
    void userEndpointWithInvalidTokenShouldFail() throws Exception {
        mockMvc.perform(get("/user/test")
                        .header("Authorization", "Bearer invalid.token.value")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        System.out.println("🧨 잘못된 토큰 요청 → 401 Unauthorized 확인 완료");
    }

    @Test
    void userEndpointWithExpiredTokenShouldFail() throws Exception {
        // 만료된 토큰 생성
        String expiredToken = Jwts.builder()
                .subject("expiredUser")
                .claim("email", "old@test.com")
                .claim("role", "USER")
                .issuedAt(new java.util.Date(System.currentTimeMillis() - 10_000))
                .expiration(new java.util.Date(System.currentTimeMillis() - 5_000))  // 이미 만료됨
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor("NxD@49#jKw!rMv7Up2zLt$gQpY%1fA3eTb8CkD$HzLwRm".getBytes()))
                .compact();

        mockMvc.perform(get("/user/test")
                        .header("Authorization", "Bearer " + expiredToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        System.out.println("⏰ 만료된 토큰 요청 → 401 Unauthorized 확인 완료");
    }

}

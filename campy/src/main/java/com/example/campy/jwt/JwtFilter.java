package com.example.campy.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException {

        try {
            // 현재 요청의 URI(경로)를 가져옴
            String uri = request.getRequestURI();

            // ✅ JWT 검증이 필요 없는 공개 경로는 필터를 통과시킴
            if (uri.startsWith("/login") ||                    // 로그인 페이지
                    uri.startsWith("/signup") ||                   // 회원가입 페이지
                    uri.startsWith("/css") ||                      // CSS 정적 파일
                    uri.startsWith("/js") ||                       // JS 정적 파일
                    uri.startsWith("/images") ||                   // 이미지 정적 파일
                    uri.equals("/favicon.ico") ||                  // 파비콘
                    uri.startsWith("/api/auth")) {                 // 로그인/회원가입 관련 API
                // JWT 검증 없이 그대로 다음 필터로 넘김
                filterChain.doFilter(request, response);
                return;
            }

            // JWT 토큰을 요청에서 추출 (Header 또는 쿠키)
            String token = resolveToken(request);

            // 토큰이 존재할 경우에만 검증 수행
            if (token != null) {
                // 토큰이 유효하다면
                if (jwtUtil.validateToken(token)) {
                    // 토큰에서 사용자 정보와 권한을 추출
                    String username = jwtUtil.getUsername(token);
                    String role = jwtUtil.getRole(token);

                    // 인증 객체를 생성 (스프링 시큐리티용)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    // ROLE_ 접두어를 붙여 스프링 시큐리티가 인식할 수 있도록 함
                                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role))
                            );

                    // 시큐리티 컨텍스트에 인증 정보 등록 → 이후 컨트롤러에서 로그인 사용자로 인식됨
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // 토큰이 유효하지 않으면 401 에러 반환
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
                    return;
                }
            }

            // 토큰이 없거나(공개 경로 제외), 인증 성공했으면 다음 필터로 넘김
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // 예외가 발생하면 401 에러 반환
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + e.getMessage());
        }
    }


    // Authorization 헤더 또는 쿠키에서 Bearer 토큰을 추출
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        // Try to get token from cookie
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwtToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

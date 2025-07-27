package com.example.campy.jwt;

import com.example.campy.service.CustomUserDetails;
import com.example.campy.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        System.out.println("🔥 JwtFilter 진입: " + request.getRequestURI());

        try {
            String token = resolveToken(request);
            System.out.println("🔥 추출한 토큰: " + token);

            // 토큰이 존재하고 유효한 경우에만 인증 처리
            if (token != null && jwtUtil.validateToken(token)) {
                System.out.println("✅ 토큰 유효함");

                // 토큰에서 사용자 정보(username)를 추출
                String username = jwtUtil.getUsername(token);

                // CustomUserDetailsService를 통해 DB에서 사용자 정보 로드
                CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

                // 인증 객체를 생성 (Principal: userDetails, Authorities: userDetails.getAuthorities())
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, // Principal로 CustomUserDetails 객체 전체를 사용
                                null,
                                userDetails.getAuthorities() // UserDetails에서 권한 정보 가져오기
                        );

                // 시큐리티 컨텍스트에 인증 정보 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("✅ 인증 객체 등록 완료: " + username);
            } else {
                System.out.println("❌ 토큰이 null이거나 유효하지 않음");
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("❌ 필터에서 예외 발생: " + e.getMessage());
            // 클라이언트에게는 표준 401 Unauthorized 에러를 반환
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid Token");
        }
    }

    /**
     * Request Header나 쿠키에서 토큰을 추출하는 메서드
     */
    private String resolveToken(HttpServletRequest request) {
        // 1. Authorization 헤더에서 토큰 찾기
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        // 2. 쿠키에서 토큰 찾기
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwtToken")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * 특정 경로에 대해서는 필터를 실행하지 않도록 설정
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 로그인 경로는 필터링에서 제외
        return path.equals("/api/auth/login");
    }
}
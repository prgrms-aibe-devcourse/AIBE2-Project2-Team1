package com.example.campy.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        System.out.println("🔥 JwtFilter 진입: " + request.getRequestURI());

        try {
            String token = resolveToken(request);
            System.out.println("🔥 추출한 토큰: " + token);

            if (token != null && jwtUtil.validateToken(token)) {
                System.out.println("✅ 토큰 유효함");

                String username = jwtUtil.getUsername(token);
                String role = jwtUtil.getRole(token);
                System.out.println("Jwt에서 뽑은 role 원본: " + role);

                // 강제로 ROLE_ 붙이기
                if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }

                // 여기를 확실하게 변경 (AuthorityUtils → SimpleGrantedAuthority)
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("✅ 인증 객체 등록 완료: " + authorities);
            } else {
                System.out.println("❌ 토큰이 null이거나 유효하지 않음");
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("❌ 필터에서 예외 발생: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + e.getMessage());
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwtToken")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/login");
    }
}
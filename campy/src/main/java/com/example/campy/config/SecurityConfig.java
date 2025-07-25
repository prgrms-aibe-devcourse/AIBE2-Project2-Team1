package com.example.campy.config;

import com.example.campy.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()

                        // ✅ Talent - GET은 허용, POST는 권한 필요
                        .requestMatchers(HttpMethod.GET, "/api/talents/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/talents/**").hasAnyAuthority("ROLE_USER", "ROLE_MENTOR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/talents/**").hasAnyAuthority("ROLE_USER", "ROLE_MENTOR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/talents/**").hasAnyAuthority("ROLE_USER", "ROLE_MENTOR", "ROLE_ADMIN")

                        // 쪽지 기능
                        .requestMatchers("/api/messages/**").authenticated()

                        // 기본 public 경로들
                        .requestMatchers("/", "/login", "/signup", "/api/auth/**", "/css/**", "/images/**", "/js/**", "/favicon.ico").permitAll()

                        // 마이페이지 등 페이지별 권한
                        .requestMatchers("/mypage").hasAnyAuthority("ROLE_USER", "ROLE_MENTOR", "ROLE_ADMIN")
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_MENTOR")

                        // 그 외 나머지는 인증 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
package com.example.campy.config;

import com.example.campy.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/api/auth/**").permitAll()
                        .requestMatchers("/api/talents/**").permitAll() // 💡 임시 오픈 (수정해야함) 전제조회, 단건조회, 등록, 수정, 삭제 테스트
                        .requestMatchers("/", "/login", "/signup", "/api/auth/**", "/css/**", "/images/**", "/js/**", "/favicon.ico").permitAll()
                        .requestMatchers("/mypage").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        // 🔓 공개 API
                        .requestMatchers(HttpMethod.GET, "/api/materials").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/materials/{id}").permitAll()

                        // 🔐 인증이 필요한 GET 요청
                        .requestMatchers(HttpMethod.GET, "/api/materials/my").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/materials/*/download").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
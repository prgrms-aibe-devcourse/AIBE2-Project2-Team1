package com.example.campy.service;

import com.example.campy.dto.LoginForm;
import com.example.campy.dto.SignUpForm;
import com.example.campy.entity.User;
import com.example.campy.jwt.JwtUtil;
import com.example.campy.repository.AdminRepository;
import com.example.campy.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(LoginForm form) {
        // 1. User 테이블에서 사용자 찾기
        return userRepository.findByUsername(form.getUsername())
                .map(user -> {
                    if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
                        throw new IllegalArgumentException("로그인 정보가 일치하지 않습니다.");
                    }
                    return jwtUtil.createToken(user.getUsername(), user.getEmail(), user.getRole());
                })
                .orElseGet(() -> {
                    // 2. User 테이블에 없으면 Admin 테이블에서 관리자 찾기
                    return adminRepository.findByUsername(form.getUsername())
                            .map(admin -> {
                                if (!passwordEncoder.matches(form.getPassword(), admin.getPassword())) {
                                    throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                                }
                                // Admin 엔티티에는 email 필드가 없으므로, username을 email 자리에 사용
                                return jwtUtil.createToken(admin.getUsername(), admin.getUsername(), admin.getLevel());
                            })
                            .orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));
                });
    }

    public void signUp(SignUpForm form) {
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        User user = User.builder()
                .username(form.getUsername())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .name(form.getName())
                .nickname(form.getNickname())
                .school(form.getSchool())
                .major(form.getMajor())
                .entranceYear(form.getEntranceYear())
                .role("USER")
                .build();

        userRepository.save(user);
    }
}

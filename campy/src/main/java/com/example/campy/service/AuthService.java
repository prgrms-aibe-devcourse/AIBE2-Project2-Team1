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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(LoginForm form) {
        // 1. User 테이블에서 사용자 찾기
        Optional<User> userOptional = userRepository.findByUsername(form.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            return jwtUtil.createToken(user.getUserId(), user.getUsername(), user.getEmail(), user.getRole());
        }

        // 2. User 테이블에 없으면 Admin 테이블에서 관리자 찾기
        return adminRepository.findByUsername(form.getUsername())
                .map(admin -> {
                    if (!passwordEncoder.matches(form.getPassword(), admin.getPassword())) {
                        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                    }
                    // Admin 엔티티에는 email 필드가 없으므로, username을 email 자리에 사용
                    return jwtUtil.createToken(admin.getAdminId(), admin.getUsername(), admin.getUsername(), admin.getLevel());
                })
                .orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));
    }

    public void signUp(SignUpForm form) {
        if (userRepository.findByUsername(form.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
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
                .role("ROLE_USER")
                .isVerified(true)
                .build();

        userRepository.save(user);
    }
}
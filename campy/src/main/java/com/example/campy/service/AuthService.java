package com.example.campy.service;

import com.example.campy.dto.LoginForm;
import com.example.campy.dto.SignUpForm;
import com.example.campy.entity.User;
import com.example.campy.jwt.JwtUtil;
import com.example.campy.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(LoginForm form) {
        User user = userRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(user.getName(), user.getEmail(), user.getRole());
    }

    public void signUp(SignUpForm form) {
        if (userRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        User user = User.builder()
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .name(form.getName())
                .nickname(form.getNickname())
                .role("USER")
                .build();

        userRepository.save(user);
    }
}

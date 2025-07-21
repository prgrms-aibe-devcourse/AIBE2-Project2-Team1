package com.example.campy.config;

import com.example.campy.entity.Admin;
import com.example.campy.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 관리자 계정이 존재하는지 확인 (username: root)
        if (adminRepository.findByUsername("root").isEmpty()) {
            // 관리자 계정이 없으면 생성
            Admin admin = Admin.builder()
                    .username("root") // 아이디를 "root"로 설정
                    .password(passwordEncoder.encode("1234")) // 비밀번호 "1234" 암호화
                    .level("ADMIN") // 레벨을 "ADMIN"으로 설정
                    .createdAt(LocalDateTime.now())
                    .build();
            adminRepository.save(admin);
            System.out.println("관리자 계정 (username: root)이 생성되었습니다.");
        }
    }
}

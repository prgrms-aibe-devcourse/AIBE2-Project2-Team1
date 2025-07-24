package com.example.campy.service;

import com.example.campy.entity.Admin;
import com.example.campy.entity.User;
import com.example.campy.repository.AdminRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. User 테이블에서 사용자 찾기
        return userRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseGet(() -> {
                    // 2. User 테이블에 없으면 Admin 테이블에서 관리자 찾기
                    return adminRepository.findByUsername(username)
                            .map(CustomUserDetails::new)
                            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
                });
    }
}
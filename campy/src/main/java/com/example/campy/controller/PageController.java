package com.example.campy.controller;

import com.example.campy.repository.UserRepository;
import com.example.campy.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/") // 기본 경로 설정
public class PageController {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @GetMapping
    public String mainPage(Authentication authentication, Model model) {
        System.out.println("mainPage 호출됨");
        if (authentication != null) {
            System.out.println("Authentication 객체 존재. Principal: " + authentication.getPrincipal() + ", Authenticated: " + authentication.isAuthenticated());
            if (authentication.isAuthenticated()) {
                String username = authentication.getName();
                System.out.println("인증된 사용자: " + username);
                userRepository.findByUsername(username).ifPresentOrElse(user -> {
                    model.addAttribute("loggedInUser", user);
                    System.out.println("loggedInUser 모델에 추가됨: " + user.getUsername());
                }, () -> {
                    System.out.println("DB에서 사용자 " + username + "를 찾을 수 없음.");
                });
            } else {
                System.out.println("Authentication 객체는 존재하지만 인증되지 않음.");
            }
        } else {
            System.out.println("Authentication 객체 없음 (로그인되지 않은 상태).");
        }
        return "main"; // → templates/main.html 렌더링
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login/login"; // → templates/login/login.html 렌더링
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup/signup"; // → templates/signup/signup.html 렌더링
    }

    @GetMapping("/mypage")
    public String myPage(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName(); // 현재 로그인한 사용자의 username
            userRepository.findByUsername(username).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }
        return "mypage/mypage"; // → templates/mypage/mypage.html 렌더링
    }

    @GetMapping("/admin")
    public String adminPage(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            adminRepository.findByUsername(username).ifPresent(admin -> {
                model.addAttribute("admin", admin);
            });
        }
        return "admin/admin"; // → templates/admin/admin.html 렌더링
    }
}
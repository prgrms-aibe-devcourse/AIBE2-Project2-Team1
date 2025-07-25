package com.example.campy.controller;

import com.example.campy.repository.UserRepository;
import com.example.campy.repository.AdminRepository;
import com.example.campy.service.CustomUserDetails;
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

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                model.addAttribute("loggedInUsername", userDetails.getUsername());
            }

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
}
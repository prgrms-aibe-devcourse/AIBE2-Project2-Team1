package com.example.campy.controller;

import com.example.campy.repository.UserRepository;
import com.example.campy.repository.AdminRepository;
import com.example.campy.service.CustomUserDetails;
import com.example.campy.service.TalentService;
import com.example.campy.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/") // 기본 경로 설정
public class PageController {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final TalentService talentService;

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
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found for username: " + username)); // 사용자를 찾지 못하면 예외 발생
            model.addAttribute("user", user);
        } else {
            // 인증되지 않은 사용자가 마이페이지에 접근 시 로그인 페이지로 리다이렉트 또는 에러 처리
            // SecurityConfig에서 이미 처리하고 있으므로 여기서는 생략 가능하지만, 명시적으로 처리할 수도 있습니다.
            return "redirect:/login";
        }
        return "mypage/mypage"; // → templates/mypage/mypage.html 렌더링
    }


}
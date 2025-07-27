package com.example.campy.controller;

import com.example.campy.dto.talent.request.TalentCreateRequest;
import com.example.campy.dto.talent.response.TalentResponseDto;
import com.example.campy.entity.Talent;
import com.example.campy.repository.UserRepository;
import com.example.campy.repository.AdminRepository;
import com.example.campy.service.CustomUserDetails;
import com.example.campy.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            userRepository.findByUsername(username).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }
        return "mypage/mypage"; // → templates/mypage/mypage.html 렌더링
    }

    @GetMapping("/talents")
    public String talentListPage(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "12") int size,
                                 Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // null 넣어서 필터 없이 전체 리스트 받기
        Page<TalentResponseDto> talents = talentService.getAllTalents(pageable, null, null, null);

        model.addAttribute("talents", talents.getContent());
        return "talents/list";
    }

    @GetMapping("/talents/register")
    public String talentRegisterPage() {
        return "talents/register"; // → templates/talents/register.html 렌더링
    }

    @PostMapping("/talents/register")
    public String submitTalentForm(
            @ModelAttribute TalentCreateRequest talentCreateRequest,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            Authentication authentication
    ) throws IOException {
        talentService.createTalent(talentCreateRequest, thumbnail, authentication);
        return "redirect:/talents/success";
    }

    @GetMapping("/talents/success")
    public String talentRegisterSuccess() {
        return "talents/register-success"; // templates/talents/register-success.html
    }

    @GetMapping("/talents/{talentId}")
    public String talentDetailPage(@PathVariable Integer talentId, Model model) {
        TalentResponseDto talent = talentService.getTalentById(talentId);
        model.addAttribute("talent", talent);
        return "talents/detail";  // → templates/talents/detail.html
    }
}
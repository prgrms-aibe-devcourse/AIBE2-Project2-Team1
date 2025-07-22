package com.example.campy.controller;

import com.example.campy.service.AdminService;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdminRepository adminRepository;

    @GetMapping
    public String adminPage(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            adminRepository.findByUsername(username).ifPresent(admin -> {
                model.addAttribute("admin", admin);
            });
        }
        return "admin/admin";
    }

    @GetMapping("/reviews")
    public String getReviews(Model model) {
        List<ReviewResponseDto> reviews = adminService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "admin/admin_reviews"; // admin/admin_reviews.html 템플릿 렌더링
    }
}

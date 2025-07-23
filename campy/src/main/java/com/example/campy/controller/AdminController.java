package com.example.campy.controller;

import com.example.campy.service.AdminService;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String getReviews(@RequestParam(value = "searchType", required = false) String searchType,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             Model model) {
        List<ReviewResponseDto> reviews = adminService.getAllReviews(searchType, keyword);
        model.addAttribute("reviews", reviews);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "admin/admin_reviews/admin_reviews";
    }

    @GetMapping("/reviews/{reviewId}/edit")
    public String editReviewForm(@PathVariable Integer reviewId, Model model) {
        ReviewResponseDto review = adminService.getReviewById(reviewId);
        model.addAttribute("review", review);
        return "admin/admin_reviews/admin_review_edit";
    }

    @PostMapping("/reviews/{reviewId}/update")
    public String updateReview(@PathVariable Integer reviewId, @ModelAttribute ReviewUpdateRequest request, RedirectAttributes redirectAttributes) {
        adminService.updateReview(reviewId, request);
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 수정되었습니다.");
        return "redirect:/admin/reviews";
    }

    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Integer reviewId, RedirectAttributes redirectAttributes) {
        adminService.deleteReview(reviewId);
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
        return "redirect:/admin/reviews";
    }

    
}

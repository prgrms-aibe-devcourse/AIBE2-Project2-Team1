package com.example.campy.controller;

import com.example.campy.service.AdminService;

import com.example.campy.service.AdminService;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.user.response.UserResponseDto;
import com.example.campy.dto.user.request.UserUpdateRequest;
import com.example.campy.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
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


    @GetMapping("/users")
    public String adminUsersPage(Model model) {
        List<UserResponseDto> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin_users/admin_users";
    }

    @GetMapping("/users/{userId}/edit")
    public String editUserForm(@PathVariable Integer userId, Model model) {
        UserResponseDto userResponseDto = adminService.getUserById(userId);
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .username(userResponseDto.username())
                .email(userResponseDto.email())
                .name(userResponseDto.name())
                .nickname(userResponseDto.nickname())
                .major(userResponseDto.major())
                .school(userResponseDto.school())
                .entranceYear(userResponseDto.entranceYear())
                .role(userResponseDto.role())
                .isVerified(userResponseDto.isVerified())
                .profileImg(userResponseDto.profileImg())
                .intro(userResponseDto.intro())
                .build();
        model.addAttribute("user", userResponseDto); // user ID for form action
        model.addAttribute("userUpdateRequest", userUpdateRequest);
        return "admin/admin_users/admin_user_edit";
    }

    @PostMapping("/users/{userId}/update")
    public String updateUser(@PathVariable Integer userId, @Valid @ModelAttribute UserUpdateRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            System.out.println("Validation Errors:");
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("user", adminService.getUserById(userId)); // 기존 사용자 정보 유지
            model.addAttribute("userUpdateRequest", request); // 입력했던 데이터 유지
            return "admin/admin_users/admin_user_edit";
        }
        adminService.updateUser(userId, request);
        redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable Integer userId, RedirectAttributes redirectAttributes) {
        adminService.deleteUser(userId);
        redirectAttributes.addFlashAttribute("message", "회원이 성공적으로 삭제되었습니다.");
        return "redirect:/admin/users";
    }

    @GetMapping("/boards")
    public String adminBoardsPage() {
        return "admin/admin_boards/admin_boards";
    }

    @GetMapping("/talents")
    public String adminTalentsPage() {
        return "admin/admin_talents/admin_talents";
    }

    @GetMapping("/mentoring-offers")
    public String adminMentoringOffersPage() {
        return "admin/admin_mentoring_offers/admin_mentoring_offers";
    }

    @GetMapping("/mentoring-matches")
    public String adminMentoringMatchesPage() {
        return "admin/admin_mentoring_matches/admin_mentoring_matches";
    }

    @GetMapping("/transactions")
    public String adminTransactionsPage() {
        return "admin/admin_transactions/admin_transactions";
    }

    @GetMapping("/ai-stats")
    public String adminAiStatsPage() {
        return "admin/admin_ai_stats/admin_ai_stats";
    }
}

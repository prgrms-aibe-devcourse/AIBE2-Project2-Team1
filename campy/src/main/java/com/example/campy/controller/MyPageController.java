package com.example.campy.controller;

import com.example.campy.dto.review.request.ReviewCreateRequest;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.user.response.UserResponseDto;
import com.example.campy.service.AdminService;
import com.example.campy.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/mypage/reviews")
@RequiredArgsConstructor
public class MyPageController {

    private final UserReviewService userReviewService;
    private final AdminService adminService; // 사용자 목록을 가져오기 위해 주입

    @GetMapping("/myreviews")
    public String getMyReviews(Authentication authentication, Model model) {
        List<ReviewResponseDto> reviews = userReviewService.getAllReviewsByUser(authentication);
        model.addAttribute("reviews", reviews);
        return "mypage/mypage_review/mypage_reviews";
    }

    @GetMapping("/new")
    public String createReviewForm(Model model) {
        model.addAttribute("reviewCreateRequest", new ReviewCreateRequest());
        List<UserResponseDto> users = adminService.getAllUsers(); // 리뷰 대상 사용자 선택을 위해 모든 사용자 목록 제공
        model.addAttribute("users", users);
        return "mypage/mypage_review/mypage_review_new";
    }

    @PostMapping("/new")
    public String createReview(@Valid @ModelAttribute ReviewCreateRequest request, BindingResult bindingResult, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<UserResponseDto> users = adminService.getAllUsers();
            model.addAttribute("users", users);
            return "mypage/mypage_review/mypage_review_new";
        }
        userReviewService.createReview(request, authentication);
        redirectAttributes.addFlashAttribute("message", "후기가 성공적으로 작성되었습니다.");
        return "redirect:/mypage/reviews/myreviews";
    }

    @GetMapping("/{reviewId}/edit")
    public String editReviewForm(@PathVariable Integer reviewId, Authentication authentication, Model model) {
        ReviewResponseDto review = userReviewService.getReviewById(reviewId, authentication);
        System.out.println("review: " + review);
        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest(review.rating(), review.category(), review.content());
        System.out.println("reviewUpdateRequest: " + reviewUpdateRequest);
        model.addAttribute("review", review);
        model.addAttribute("reviewUpdateRequest", reviewUpdateRequest);
        return "mypage/mypage_review/mypage_review_edit";
    }

    @PostMapping("/{reviewId}/update")
    public String updateReview(@PathVariable Integer reviewId, @ModelAttribute ReviewUpdateRequest request, Authentication authentication, RedirectAttributes redirectAttributes) {
        userReviewService.updateReview(reviewId, request, authentication);
        redirectAttributes.addFlashAttribute("message", "후기가 성공적으로 수정되었습니다.");
        return "redirect:/mypage/reviews/myreviews";
    }

    @PostMapping("/{reviewId}/delete")
    public String deleteReview(@PathVariable Integer reviewId, Authentication authentication, RedirectAttributes redirectAttributes) {
        userReviewService.deleteReview(reviewId, authentication);
        redirectAttributes.addFlashAttribute("message", "후기가 성공적으로 삭제되었습니다.");
        return "redirect:/mypage/reviews/myreviews";
    }
}

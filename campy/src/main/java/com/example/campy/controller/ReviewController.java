package com.example.campy.controller;

import com.example.campy.dto.review.request.ReviewCreateRequest;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.service.AdminService;
import com.example.campy.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final AdminService adminService;
    private final UserReviewService userReviewService;

    @GetMapping
    public String getAllReviews(@RequestParam(value = "type", required = false) String type,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                Model model) {

        List<ReviewResponseDto> reviews = adminService.getAllReviews(type, keyword);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewCreateRequest", new ReviewCreateRequest()); // 모달용 빈 객체
        return "review/listReview";
    }

    @PostMapping
    public String createReview(@ModelAttribute ReviewCreateRequest request, Authentication authentication) {
        userReviewService.createReview(request, authentication);
        return "redirect:/reviews";
    }

    @GetMapping("/api/by-target")
    @ResponseBody
    public List<ReviewResponseDto> getReviewsByTarget(@RequestParam Integer targetId, @RequestParam String category) {
        return userReviewService.getReviewsByTargetIdAndCategory(targetId, category);
    }
}


package com.example.campy.controller;

import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final AdminService adminService;

    @GetMapping
    public String getAllReviews(Model model) {
        List<ReviewResponseDto> reviews = adminService.getAllReviews(null, null); // 모든 리뷰 조회
        model.addAttribute("reviews", reviews);
        return "reviews/reviews_board";
    }
}

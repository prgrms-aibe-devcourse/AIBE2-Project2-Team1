package com.example.campy.controller;

import com.example.campy.dto.review.request.ReviewCreateRequest;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-reviews")
public class UserReviewController {

    private final UserReviewService userReviewService;

    // 내 리뷰 생성
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createMyReview(@RequestBody ReviewCreateRequest request, Authentication authentication) {
        ReviewResponseDto createdReview = userReviewService.createReview(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    // 내 리뷰 단건 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getMyReviewById(@PathVariable Integer reviewId, Authentication authentication) {
        ReviewResponseDto review = userReviewService.getReviewById(reviewId, authentication);
        return ResponseEntity.ok(review);
    }

    // 내 모든 리뷰 조회
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllMyReviews(Authentication authentication) {
        List<ReviewResponseDto> reviews = userReviewService.getAllReviewsByUser(authentication);
        return ResponseEntity.ok(reviews);
    }

    // 내 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateMyReview(@PathVariable Integer reviewId, @RequestBody ReviewUpdateRequest request, Authentication authentication) {
        ReviewResponseDto updatedReview = userReviewService.updateReview(reviewId, request, authentication);
        return ResponseEntity.ok(updatedReview);
    }

    // 내 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteMyReview(@PathVariable Integer reviewId, Authentication authentication) {
        userReviewService.deleteReview(reviewId, authentication);
        return ResponseEntity.noContent().build();
    }
}

package com.example.campy.service;

import com.example.campy.dto.review.request.ReviewCreateRequest;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.review.response.ReviewResponseDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserReviewService {
    ReviewResponseDto createReview(ReviewCreateRequest request, Authentication authentication);
    ReviewResponseDto getReviewById(Integer reviewId, Authentication authentication);
    List<ReviewResponseDto> getAllReviewsByUser(Authentication authentication);
    ReviewResponseDto updateReview(Integer reviewId, ReviewUpdateRequest request, Authentication authentication);
    void deleteReview(Integer reviewId, Authentication authentication);
}

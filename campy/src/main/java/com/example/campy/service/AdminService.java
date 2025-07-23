package com.example.campy.service;

import com.example.campy.repository.ReviewRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.dto.user.response.UserResponseDto;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.entity.Review;
import com.example.campy.entity.User;
import com.example.campy.exception.GeneralException;
import com.example.campy.constant.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDto> getAllReviews(String searchType, String keyword) {
        List<Review> reviews;

        if (keyword == null || keyword.trim().isEmpty()) {
            reviews = reviewRepository.findByDeletedAtIsNull();
        } else {
            switch (searchType) {
                case "content":
                    reviews = reviewRepository.findByContentContainingIgnoreCaseAndDeletedAtIsNull(keyword);
                    break;
                case "category":
                    reviews = reviewRepository.findByCategoryContainingIgnoreCaseAndDeletedAtIsNull(keyword);
                    break;
                case "authorNickname":
                    reviews = reviewRepository.findByAuthor_NicknameContainingIgnoreCaseAndDeletedAtIsNull(keyword);
                    break;
                case "targetUserNickname":
                    reviews = reviewRepository.findByTargetUser_NicknameContainingIgnoreCaseAndDeletedAtIsNull(keyword);
                    break;
                default:
                    reviews = reviewRepository.findByDeletedAtIsNull(); // Fallback to all non-deleted if searchType is invalid
                    break;
            }
        }
        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    public ReviewResponseDto getReviewById(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorCode.REVIEW_NOT_FOUND));
        return ReviewResponseDto.from(review);
    }

    @Transactional
    public void updateReview(Integer reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorCode.REVIEW_NOT_FOUND));

        review.update(request.getRating(), request.getCategory(), request.getContent());
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorCode.REVIEW_NOT_FOUND));
        review.setDeletedAt(java.time.LocalDateTime.now());
        reviewRepository.save(review);
    }


}

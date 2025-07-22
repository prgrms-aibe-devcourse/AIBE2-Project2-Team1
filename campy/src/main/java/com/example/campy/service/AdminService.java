package com.example.campy.service;

import com.example.campy.repository.ReviewRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.review.request.ReviewCreateRequest;
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

    public List<ReviewResponseDto> getAllReviews(String searchType, String keyword) {
        List<Review> reviews;

        if (keyword == null || keyword.trim().isEmpty()) {
            reviews = reviewRepository.findAll();
        } else {
            switch (searchType) {
                case "content":
                    reviews = reviewRepository.findByContentContainingIgnoreCase(keyword);
                    break;
                case "category":
                    reviews = reviewRepository.findByCategoryContainingIgnoreCase(keyword);
                    break;
                case "authorNickname":
                    reviews = reviewRepository.findByAuthor_NicknameContainingIgnoreCase(keyword);
                    break;
                case "targetUserNickname":
                    reviews = reviewRepository.findByTargetUser_NicknameContainingIgnoreCase(keyword);
                    break;
                default:
                    reviews = reviewRepository.findAll(); // Fallback to all if searchType is invalid
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

    @Transactional
    public void createReview(ReviewCreateRequest request) {
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
        User targetUser = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        Review review = Review.builder()
                .author(author)
                .targetUser(targetUser)
                .targetId(request.getTargetId())
                .rating(request.getRating())
                .category(request.getCategory())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        reviewRepository.save(review);
    }
}

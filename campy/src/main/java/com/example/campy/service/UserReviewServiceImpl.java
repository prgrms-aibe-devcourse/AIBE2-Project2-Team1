package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.dto.review.request.ReviewCreateRequest;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.entity.Review;
import com.example.campy.entity.User;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.ReviewRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserReviewServiceImpl implements UserReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "현재 로그인한 사용자를 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(ReviewCreateRequest request, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);

        // 리뷰 대상 사용자 (targetUser)는 request에서 받아옴
        User targetUser = userRepository.findById(request.targetUserId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "리뷰 대상 사용자를 찾을 수 없습니다."));

        Review newReview = Review.builder()
                .author(currentUser) // 현재 로그인한 사용자가 작성자
                .targetUser(targetUser)
                .targetId(request.targetId())
                .rating(request.rating())
                .category(request.category())
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(newReview);
        return ReviewResponseDto.from(savedReview);
    }

    @Override
    public ReviewResponseDto getReviewById(Integer reviewId, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorCode.REVIEW_NOT_FOUND));

        // 현재 사용자가 리뷰의 작성자인지 확인
        if (!review.getAuthor().getUserId().equals(currentUser.getUserId())) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED, "해당 리뷰를 조회할 권한이 없습니다.");
        }
        return ReviewResponseDto.from(review);
    }

    @Override
    public List<ReviewResponseDto> getAllReviewsByUser(Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        return reviewRepository.findByAuthorAndDeletedAtIsNull(currentUser).stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewResponseDto updateReview(Integer reviewId, ReviewUpdateRequest request, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorCode.REVIEW_NOT_FOUND));

        // 현재 사용자가 리뷰의 작성자인지 확인
        if (!review.getAuthor().getUserId().equals(currentUser.getUserId())) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED, "해당 리뷰를 수정할 권한이 없습니다.");
        }

        review.update(request.getRating(), request.getCategory(), request.getContent());
        review.setUpdatedAt(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(review);
        return ReviewResponseDto.from(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorCode.REVIEW_NOT_FOUND));

        // 현재 사용자가 리뷰의 작성자인지 확인
        if (!review.getAuthor().getUserId().equals(currentUser.getUserId())) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED, "해당 리뷰를 삭제할 권한이 없습니다.");
        }

        review.setDeletedAt(LocalDateTime.now()); // 소프트 삭제
        reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponseDto> getReviewsByTargetIdAndCategory(Integer targetId, String category) {
        return reviewRepository.findByTargetIdAndCategoryAndDeletedAtIsNull(targetId, category).stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }
}

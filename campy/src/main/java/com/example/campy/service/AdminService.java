package com.example.campy.service;

import com.example.campy.repository.ReviewRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.dto.user.response.UserResponseDto;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.user.request.UserUpdateRequest;
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
        return userRepository.findByDeletedAtIsNull().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
        return UserResponseDto.from(user);
    }

    @Transactional
    public void updateUser(Integer userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setNickname(request.getNickname());
        user.setMajor(request.getMajor());
        user.setSchool(request.getSchool());
        user.setEntranceYear(request.getEntranceYear());
        user.setRole(request.getRole());
        user.setIsVerified(request.getIsVerified());
        user.setProfileImg(request.getProfileImg());
        user.setIntro(request.getIntro());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 해당 유저가 작성했거나 대상이 된 리뷰들을 소프트 삭제
        List<Review> authoredReviews = reviewRepository.findByAuthorAndDeletedAtIsNull(user);
        authoredReviews.forEach(review -> {
            review.setDeletedAt(LocalDateTime.now());
            reviewRepository.save(review);
        });

        List<Review> targetedReviews = reviewRepository.findByTargetUserAndDeletedAtIsNull(user);
        targetedReviews.forEach(review -> {
            review.setDeletedAt(LocalDateTime.now());
            reviewRepository.save(review);
        });

        // 유저 소프트 삭제
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
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

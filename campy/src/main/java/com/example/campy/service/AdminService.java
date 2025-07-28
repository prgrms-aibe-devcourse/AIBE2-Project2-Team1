package com.example.campy.service;

import com.example.campy.repository.ReviewRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.dto.user.response.UserResponseDto;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.review.request.ReviewCreateRequest;
import com.example.campy.dto.user.request.UserUpdateRequest;
import com.example.campy.dto.user.request.UserCreateRequest;
import com.example.campy.entity.Review;
import com.example.campy.entity.User;
import com.example.campy.exception.GeneralException;
import com.example.campy.constant.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

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

        user.update(
                request.getUsername(),
                request.getEmail(),
                request.getName(),
                request.getNickname(),
                request.getMajor(),
                request.getSchool(),
                request.getEntranceYear(),
                request.getRole(),
                request.getIsVerified() != null ? request.getIsVerified() : false,
                request.getProfileImg(),
                request.getIntro()
        );
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

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
                case "reviewer":
                    reviews = reviewRepository.findByAuthor_NicknameContainingIgnoreCaseAndDeletedAtIsNull(keyword);
                    break;
                default:
                    reviews = reviewRepository.findByDeletedAtIsNull();
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
        review.setDeletedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    // 리뷰 생성 - 닉네임 기반 대상자 찾기
    @Transactional
    public ReviewResponseDto createReview(ReviewCreateRequest request) {
        // 작성자는 admin 사용자로 고정
        User author = userRepository.findByUsername("admin")
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "작성자(admin)를 찾을 수 없습니다."));

        // 대상 유저는 nickname으로 찾음
        User targetUser = userRepository.findByNickname(request.nickname())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "대상 닉네임 사용자를 찾을 수 없습니다."));

        Review newReview = Review.builder()
                .author(author)
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

    @Transactional
    public UserResponseDto createUser(UserCreateRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new GeneralException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .password(encodedPassword)
                .name(request.name())
                .nickname(request.nickname())
                .school(request.school())
                .major(request.major())
                .entranceYear(request.entranceYear())
                .role("USER")
                .isVerified(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(newUser);
        return UserResponseDto.from(savedUser);
    }
}
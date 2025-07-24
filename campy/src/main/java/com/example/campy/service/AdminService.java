package com.example.campy.service;

import com.example.campy.repository.ReviewRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.dto.user.response.UserResponseDto;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.review.request.ReviewCreateRequest;
import com.example.campy.dto.user.request.UserUpdateRequest;
import com.example.campy.dto.user.request.UserCreateRequest; // UserCreateRequest import 추가
import com.example.campy.entity.Review;
import com.example.campy.entity.User;
import com.example.campy.exception.GeneralException;
import com.example.campy.constant.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // PasswordEncoder import 추가
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
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입

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

    // 새로운 리뷰 생성 메서드 추가
    @Transactional
    public ReviewResponseDto createReview(ReviewCreateRequest request) {
        User author = userRepository.findById(request.authorId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "작성자 사용자를 찾을 수 없습니다."));
        User targetUser = userRepository.findById(request.targetUserId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "대상 사용자를 찾을 수 없습니다."));

        Review newReview = Review.builder()
                .author(author)
                .targetUser(targetUser)
                .targetId(request.targetId())
                .rating(request.rating())
                .category(request.category())
                .content(request.content())
                .createdAt(LocalDateTime.now()) // 생성 시간 설정
                .updatedAt(LocalDateTime.now()) // 업데이트 시간 설정
                .build();

        Review savedReview = reviewRepository.save(newReview);
        return ReviewResponseDto.from(savedReview);
    }

    // 새로운 사용자 생성 메서드 추가
    @Transactional
    public UserResponseDto createUser(UserCreateRequest request) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new GeneralException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
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
                .role("USER") // 기본 역할 부여
                .isVerified(false) // 기본적으로 미인증 상태
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(newUser);
        return UserResponseDto.from(savedUser);
    }
}

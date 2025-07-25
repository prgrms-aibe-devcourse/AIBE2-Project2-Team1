package com.example.campy.dto.review.response;

import com.example.campy.entity.Review;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ReviewResponseDto(
    Integer reviewId,
    Integer rating,
    String category,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    AuthorDto author,
    TargetUserDto targetUser,
    Integer targetId
) {
    public static ReviewResponseDto from(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .rating(review.getRating())
                .category(review.getCategory())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .author(AuthorDto.from(review.getAuthor()))
                .targetUser(TargetUserDto.from(review.getTargetUser()))
                .targetId(review.getTargetId())
                .build();
    }

    @Builder
    public record AuthorDto(
        Integer userId,
        String nickname
    ) {
        public static AuthorDto from(com.example.campy.entity.User user) {
            return AuthorDto.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .build();
        }
    }

    @Builder
    public record TargetUserDto(
        Integer userId,
        String nickname
    ) {
        public static TargetUserDto from(com.example.campy.entity.User user) {
            return TargetUserDto.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .build();
        }
    }
}

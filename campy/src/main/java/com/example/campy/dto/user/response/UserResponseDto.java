package com.example.campy.dto.user.response;

import com.example.campy.entity.User;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record UserResponseDto(
    Integer userId,
    String username,
    String email,
    String name,
    String nickname,
    String major,
    String school,
    Integer entranceYear,
    String role,
    Boolean isVerified,
    String profileImg,
    String intro,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .major(user.getMajor())
                .school(user.getSchool())
                .entranceYear(user.getEntranceYear())
                .role(user.getRole())
                .isVerified(user.getIsVerified())
                .profileImg(user.getProfileImg())
                .intro(user.getIntro())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

package com.example.campy.dto.talent.response;

import com.example.campy.entity.Talent;
import com.example.campy.dto.user.response.UserResponseDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TalentResponseDto(
        Integer talentId,
        UserResponseDto user,
        String title,
        String description,
        Integer price,
        String availableDays,
        String offlineLocation,
        String status,
        Boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String imagePath,
        String category
) {
    public static TalentResponseDto fromEntity(Talent talent) {
        return TalentResponseDto.builder()
                .talentId(talent.getTalentId())
                .user(UserResponseDto.fromEntity(talent.getUser()))
                .title(talent.getTitle())
                .description(talent.getDescription())
                .price(talent.getPrice())
                .availableDays(talent.getAvailableDays())
                .offlineLocation(talent.getOfflineLocation())
                .status(talent.getStatus())
                .isDeleted(talent.getIsDeleted())
                .createdAt(talent.getCreatedAt())
                .updatedAt(talent.getUpdatedAt())
                .imagePath(talent.getImagePath())
                .category(talent.getCategory())
                .build();
    }
}

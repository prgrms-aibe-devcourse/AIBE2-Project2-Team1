package com.example.campy.dto.talent.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TalentCreateRequest(
        @NotNull(message = "사용자 ID는 필수입니다.")
        Integer userId,
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        @NotBlank(message = "설명은 필수입니다.")
        String description,
        @NotNull(message = "가격은 필수입니다.")
        Integer price,
        String availableDays,
        String offlineLocation,
        String status,
        String imagePath,
        @NotBlank(message = "카테고리는 필수입니다.")
        String category
) {}

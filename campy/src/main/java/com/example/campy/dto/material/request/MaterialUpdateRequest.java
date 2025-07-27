package com.example.campy.dto.material.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record MaterialUpdateRequest(
        @NotNull(message = "자료 ID는 필수입니다.")
        Integer materialId,

        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        String content,

        @NotNull(message = "가격은 필수입니다.")
        Integer price,

        @NotNull(message = "삭제 여부는 필수입니다.")
        Boolean isDeleted
) {
    @Builder
    public MaterialUpdateRequest {}
}
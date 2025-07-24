package com.example.campy.dto.review.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record ReviewCreateRequest(
        @NotNull(message = "작성자 ID는 필수입니다.")
        Integer authorId,

        @NotNull(message = "대상 사용자 ID는 필수입니다.")
        Integer targetUserId,

        @NotNull(message = "대상 ID는 필수입니다.")
        Integer targetId,

        @NotNull(message = "평점은 필수입니다.")
        @Min(value = 1, message = "평점은 최소 1점 이상이어야 합니다.")
        Integer rating,

        @NotBlank(message = "카테고리는 필수입니다.")
        @Size(max = 20, message = "카테고리는 20자를 초과할 수 없습니다.")
        String category,

        @NotBlank(message = "내용은 필수입니다.")
        String content
) {
    @Builder
    public ReviewCreateRequest {}
}

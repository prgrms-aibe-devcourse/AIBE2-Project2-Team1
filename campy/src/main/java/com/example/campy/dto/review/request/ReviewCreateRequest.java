package com.example.campy.dto.review.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {
    @NotNull
    private Integer authorId;
    @NotNull
    private Integer targetUserId;
    private Integer targetId;
    @NotNull
    private Integer rating;
    @NotNull
    private String category;
    @NotNull
    private String content;
}

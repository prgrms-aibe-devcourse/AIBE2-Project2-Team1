package com.example.campy.dto.board.request;

import com.example.campy.entity.Board;
import com.example.campy.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

public record BoardCreateRequest(
        @NotNull(message = "작성자 ID는 필수입니다.")
        Integer userId,

        @Size(max = 100, message = "학교는 100자를 초과할 수 없습니다.")
        String school,

        @NotBlank(message = "카테고리는 필수입니다.")
        @Size(max = 30, message = "카테고리는 30자를 초과할 수 없습니다.")
        String category,

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        String content
) {
    public Board toEntity(User user) {
        return Board.builder()
                .user(user)
                .school(this.school)
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }

    @Builder
    public BoardCreateRequest {}
}


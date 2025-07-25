package com.example.campy.dto.board.response;

import com.example.campy.entity.Board;
import com.example.campy.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

public record BoardResponseDto(
        Integer boardId,
        Integer userId,
        String username,
        String school,
        String category,
        String title,
        String content,
        Boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public BoardResponseDto(Board board) {
        this(
                board.getBoardId(),
                board.getUser().getUserId(),
                board.getUser().getUsername(),
                board.getSchool(),
                board.getCategory(),
                board.getTitle(),
                board.getContent(),
                board.getIsDeleted(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }

    @Builder
    public BoardResponseDto {}
}

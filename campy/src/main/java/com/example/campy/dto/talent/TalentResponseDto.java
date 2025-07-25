package com.example.campy.dto.talent;

import com.example.campy.dto.user.response.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalentResponseDto {
    private Integer talentId;
    private String title;
    private String description;
    private Integer price;
    private String availableDays;
    private String offlineLocation;
    private String status;
    private String imagePath;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponseDto user; // User 정보를 담을 DTO
    private List<String> tagNames; // 태그 이름 목록
}

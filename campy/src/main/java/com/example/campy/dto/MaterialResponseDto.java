package com.example.campy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MaterialResponseDto {
    private Integer materialId;
    private Integer sellerId;
    private String title;
    private String content;
    private String fileUrl;
    private String previewFileUrl;
    private String thumbnailUrl;
    private Integer price;
    private LocalDateTime createdAt;
}
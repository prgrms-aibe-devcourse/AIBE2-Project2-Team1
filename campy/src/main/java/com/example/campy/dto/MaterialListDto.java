package com.example.campy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MaterialListDto {
    private Integer materialId;
    private String title;
    private String thumbnailUrl;
    private Integer price;
    private LocalDateTime createdAt;
}
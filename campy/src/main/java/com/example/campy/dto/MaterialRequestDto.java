package com.example.campy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequestDto {
    private String title;
    private String content;
    private String fileUrl;
    private String previewFileUrl;
    private String thumbnailUrl;
    private Integer price;
}
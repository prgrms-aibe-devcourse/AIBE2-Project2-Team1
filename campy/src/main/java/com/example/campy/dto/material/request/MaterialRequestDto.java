package com.example.campy.dto.material.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialRequestDto {
    private String title;
    private String content;
    private String fileUrl;
    private String previewFileUrl;
    private String thumbnailUrl;
    private Integer price;
}
package com.example.campy.dto.material;

import com.example.campy.entity.Material;
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

    public MaterialListDto(Material material) {
        this.materialId = material.getMaterialId();
        this.title = material.getTitle();
        this.thumbnailUrl = material.getThumbnailUrl();
        this.price = material.getPrice();
        this.createdAt = material.getCreatedAt();
    }
}
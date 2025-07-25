package com.example.campy.dto.material.response;

import com.example.campy.entity.Material;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MaterialResponseDto {

    private final Integer materialId;
    private final Integer sellerId;
    private final String title;
    private final String content;
    private final String fileUrl;
    private final String previewFileUrl;
    private final String thumbnailUrl;
    private final Integer price;
    private final String createdAt;

    public MaterialResponseDto(Material material) {
        this.materialId = material.getMaterialId();
        this.sellerId = material.getSeller().getUserId();
        this.title = material.getTitle();
        this.content = material.getContent();
        this.fileUrl = material.getFileUrl();
        this.previewFileUrl = material.getPreviewFileUrl();
        this.thumbnailUrl = material.getThumbnailUrl();
        this.price = material.getPrice();
        this.createdAt = material.getCreatedAt().toString(); // 필요하면 포맷 변경 가능
    }
}
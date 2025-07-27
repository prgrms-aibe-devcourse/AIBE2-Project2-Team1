package com.example.campy.dto.material.request;

import com.example.campy.entity.Material;
import com.example.campy.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MaterialCreateRequest {
    private String title;
    private String category;
    private String description;
    private Integer price;

    public Material toEntity(User seller, String fileUrl, String thumbnailUrl) {
        return Material.builder()
                .title(this.title)
                .category(this.category)
                .content(this.description) // description을 content로 매핑
                .price(this.price)
                .seller(seller)
                .fileUrl(fileUrl)
                .thumbnailUrl(thumbnailUrl)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }
}

package com.example.campy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialDto {
    private Long id;
    private String title;
    private String seller;
    private String price;
    private String uploadDate;
    private String thumbnail;
    private String category;
    private String description;

    // 기본 생성자
    public MaterialDto() {}

    // 전체 생성자
    public MaterialDto(Long id, String title, String seller, String price,
                       String uploadDate, String thumbnail, String category, String description) {
        this.id = id;
        this.title = title;
        this.seller = seller;
        this.price = price;
        this.uploadDate = uploadDate;
        this.thumbnail = thumbnail;
        this.category = category;
        this.description = description;
    }
}




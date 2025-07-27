package com.example.campy.dto.material.response;

import com.example.campy.entity.Material;
import lombok.Getter;
import lombok.Setter;


@Builder
public record MaterialResponseDto(
    Integer materialId,
    UserResponseDto seller,
    String title,
    String content,
    String category,
    String fileUrl,
    String previewFileUrl,
    String thumbnailUrl,
    Integer price,
    Boolean isDeleted,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static MaterialResponseDto from(Material material) {
        return MaterialResponseDto.builder()
                .materialId(material.getMaterialId())
                .seller(UserResponseDto.builder()
                        .userId(material.getSeller().getUserId())
                        .username(material.getSeller().getUsername())
                        .email(material.getSeller().getEmail())
                        .name(material.getSeller().getName())
                        .nickname(material.getSeller().getNickname())
                        .school(material.getSeller().getSchool())
                        .major(material.getSeller().getMajor())
                        .entranceYear(material.getSeller().getEntranceYear())
                        .role(material.getSeller().getRole())
                        .isVerified(material.getSeller().getIsVerified())
                        .profileImg(material.getSeller().getProfileImg())
                        .intro(material.getSeller().getIntro())
                        .build())
                .title(material.getTitle())
                .content(material.getContent())
                .category(material.getCategory())
                .fileUrl(material.getFileUrl())
                .previewFileUrl(material.getPreviewFileUrl())
                .thumbnailUrl(material.getThumbnailUrl())
                .price(material.getPrice())
                .isDeleted(material.getIsDeleted())
                .createdAt(material.getCreatedAt())
                .updatedAt(material.getUpdatedAt())
                .build();

    }
}
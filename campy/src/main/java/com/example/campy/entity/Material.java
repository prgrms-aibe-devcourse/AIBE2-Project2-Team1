package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

import com.example.campy.dto.material.request.MaterialUpdateRequest;
import java.time.LocalDateTime;

@Entity
@Table(name = "materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private Integer materialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "file_url", length = 255)
    private String fileUrl;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "preview_file_url", length = 255)
    private String previewFileUrl;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl;

    @Column(name = "price")
    private Integer price;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void update(MaterialUpdateRequest dto) {
        this.title = dto.title();
        this.content = dto.content();
        this.price = dto.price();
        // this.category = dto.category(); // DTO에 category 필드가 없으므로 주석 처리
        this.updatedAt = LocalDateTime.now();
        // seller, fileUrl, fileName, previewFileUrl, thumbnailUrl, isDeleted는 여기서 업데이트하지 않습니다.
    }
}
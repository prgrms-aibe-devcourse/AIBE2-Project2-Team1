package com.example.campy.entity;

import jakarta.persistence.*;
        import lombok.*;

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

    @Column(name = "seller_id", nullable = false)
    private Integer sellerId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "file_url", length = 255)
    private String fileUrl;

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
}
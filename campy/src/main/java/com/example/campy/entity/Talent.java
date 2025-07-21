package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "talents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Talent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talent_id")
    private Integer talentId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "available_days", length = 100)
    private String availableDays;

    @Column(name = "offline_location", length = 255)
    private String offlineLocation;

    @Column(name = "status", length = 20)
    private String status; // 예: 요청중, 진행중, 완료

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "image_path", length = 255)
    private String imagePath;
}

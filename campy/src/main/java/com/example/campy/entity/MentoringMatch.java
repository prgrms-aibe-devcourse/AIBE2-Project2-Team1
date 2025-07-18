package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentoring_matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Integer matchId;

    @Column(name = "request_id", nullable = false)
    private Integer requestId;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // 예: REQUESTED, ACCEPTED, REJECTED, CANCELED

    @Column(name = "type", nullable = false, length = 20)
    private String type; // 예: 개인, 그룹

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
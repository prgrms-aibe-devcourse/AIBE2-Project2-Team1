package com.example.campy.entity;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.constant.MentoringType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoring_requests_id", nullable = false)
    private MentoringOffer mentoringOffer; // MentoringRequest → MentoringOffer

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MentoringStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private MentoringType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
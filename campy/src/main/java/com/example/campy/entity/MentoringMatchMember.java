package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentoring_match_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringMatchMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "match_id", nullable = false)
    private Integer matchId;

    @Column(name = "request_id", nullable = false)
    private Integer requestId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "role", length = 20)
    private String role; // 예: MENTOR, MENTEE

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "status", length = 20)
    private String status; // 예: WAITING, ACCEPTED, REJECTED
}

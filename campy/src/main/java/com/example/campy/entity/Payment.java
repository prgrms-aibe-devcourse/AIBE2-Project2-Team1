package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "buyer_id", nullable = false)
    private Integer buyerId;

    @Column(name = "target_id")
    private Integer targetId; // 재능, 멘토링, 자료 ID 중 하나

    @Column(name = "type", length = 20)
    private String type; // 예: 멘토링, 재능, 자료

    @Column(name = "status", length = 20)
    private String status; // 예: 판매중, 판매완료

    @Column(name = "method", length = 30)
    private String method; // 예: 무통장입금, 카드결제, 카카오페이

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
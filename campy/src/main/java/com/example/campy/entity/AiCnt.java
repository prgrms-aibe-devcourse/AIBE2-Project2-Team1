package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_cnt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiCnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tag_list", length = 255)
    private String tagList;

    @Column(name = "searched_at")
    private LocalDateTime searchedAt;
}
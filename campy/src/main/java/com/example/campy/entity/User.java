package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email; // 대학 이메일 (.ac.kr)

    private String password;

    private String name;

    private String nickname;

    private String major;

    private String school;

    private Integer entranceYear;

    private String role;

    private Boolean isVerified;

    private String profileImg;

    @Column(columnDefinition = "TEXT")
    private String intro;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

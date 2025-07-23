package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "major", length = 100)
    private String major;

    @Column(name = "school", length = 100)
    private String school;

    @Column(name = "entrance_year")
    private Integer entranceYear; // 또는 YEAR 타입 → JPA에서 Integer로 매핑

    @Column(name = "role", length = 20)
    private String role;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "profile_img", length = 255)
    private String profileImg;

    @Column(name = "intro", columnDefinition = "TEXT")
    private String intro;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void update(String username, String email, String name, String nickname, String major, String school, Integer entranceYear, String role, Boolean isVerified, String profileImg, String intro) {
        this.setUsername(username);
        this.setEmail(email);
        this.setName(name);
        this.setNickname(nickname);
        this.setMajor(major);
        this.setSchool(school);
        this.setEntranceYear(entranceYear);
        this.setRole(role);
        this.setIsVerified(isVerified);
        this.setProfileImg(profileImg);
        this.setIntro(intro);
        this.setUpdatedAt(LocalDateTime.now());
    }
}


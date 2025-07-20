package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mentoring_tags_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringTagsPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentoring_tags_post_id")
    private Integer mentoringTagsPostId;

    @Column(name = "mentoring_request_id", nullable = false)
    private Integer mentoringRequestId;

    @Column(name = "mentoring_tag_id", nullable = false)
    private Integer mentoringTagId;
}
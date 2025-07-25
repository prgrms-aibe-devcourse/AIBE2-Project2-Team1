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
public class MentoringTagPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentoring_tags_post_id")
    private Integer mentoringTagsPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoring_requests_id")
    private MentoringOffer mentoringOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentoring_tag_id")
    private MentoringTag tag;
}
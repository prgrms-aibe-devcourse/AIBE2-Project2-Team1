package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "talents_tags_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TalentTagPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talent_tag_post_id")
    private Integer talentTagPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talent_tag_id", nullable = false)
    private TalentTag talentTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talent_id", nullable = false)
    private Talent talent;
}

package com.example.campy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "talents_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TalentTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talent_tag_id")
    private Integer talentTagId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
}

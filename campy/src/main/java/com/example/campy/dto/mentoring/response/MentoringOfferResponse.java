package com.example.campy.dto.mentoring.response;

import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringOfferResponse {

    private Integer offerId;
    private String title;
    private String description;
    private String status;
    private Integer price;
    private String location;
    private Integer maxParticipants;
    private Integer duration;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<String> tags;

    private String nickname;
    private String profileImg;


    public static MentoringOfferResponse from(MentoringOffer entity) {

        return MentoringOfferResponse.builder()
                .offerId(entity.getOfferId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus().getLabel())
                .price(entity.getPrice())
                .location(entity.getLocation())
                .maxParticipants(entity.getMaxParticipants())
                .duration(entity.getDuration())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static MentoringOfferResponse from(MentoringOffer entity, List<String> tagNames) {
        User user = entity.getUser();

        return MentoringOfferResponse.builder()
                .offerId(entity.getOfferId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus().getLabel())
                .price(entity.getPrice())
                .location(entity.getLocation())
                .maxParticipants(entity.getMaxParticipants())
                .duration(entity.getDuration())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .nickname(user != null ? user.getNickname() : null)
                .profileImg(user != null ? user.getProfileImg() : null)
                .tags(tagNames)
                .build();
    }



}
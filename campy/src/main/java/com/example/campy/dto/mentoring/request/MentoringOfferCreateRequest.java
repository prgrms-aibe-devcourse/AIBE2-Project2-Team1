package com.example.campy.dto.mentoring.request;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringOfferCreateRequest {

    

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Integer duration;
    private Integer price;
    private String location;
    private Integer maxParticipants;

    public MentoringOffer toEntity(User user) {
        return MentoringOffer.builder()
                .user(user) //
                .title(this.title)
                .description(this.description)
                .duration(this.duration)
                .price(this.price)
                .location(this.location)
                .maxParticipants(this.maxParticipants)
                .status(MentoringStatus.REQUESTED)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

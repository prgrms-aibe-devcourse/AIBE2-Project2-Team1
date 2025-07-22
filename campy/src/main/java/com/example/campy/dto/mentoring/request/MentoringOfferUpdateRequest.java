package com.example.campy.dto.mentoring.request;

import com.example.campy.constant.MentoringStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringOfferUpdateRequest {
    private String title;
    private String description;
    private Integer price;
    private String location;
    private Integer maxParticipants;
    private Integer duration;
    private MentoringStatus status;
    private List<String> tags;
}

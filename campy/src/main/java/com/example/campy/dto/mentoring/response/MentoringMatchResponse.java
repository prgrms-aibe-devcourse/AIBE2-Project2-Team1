package com.example.campy.dto.mentoring.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringMatchResponse {

    private Integer matchId;
    private Integer mentoringOfferId;
    private String status;
    private String type;

    private LocalDateTime createdAt;

}

package com.example.campy.dto.mentoring.response;

import com.example.campy.entity.MentoringMatchDetail;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class MentoringMatchDetailResponse {

    private Integer detailId;
    private Integer matchId; // 연관된 매칭 ID
    private String topic;
    private String summary;
    private LocalDateTime sessionDate;
    private LocalDateTime endDate;
    private Integer durationMinutes;
    private LocalDateTime createdAt;

    public static MentoringMatchDetailResponse from(MentoringMatchDetail entity) {
        return MentoringMatchDetailResponse.builder()
                .detailId(entity.getId())
                .matchId(entity.getMentoringMatch().getMatchId())
                .topic(entity.getTopic())
                .summary(entity.getSummary())
                .sessionDate(entity.getSessionDate())
                .endDate(entity.getEndDate())
                .durationMinutes(entity.getDurationMinutes())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}

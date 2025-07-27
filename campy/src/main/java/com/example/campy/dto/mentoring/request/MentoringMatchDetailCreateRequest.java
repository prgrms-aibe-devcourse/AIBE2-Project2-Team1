package com.example.campy.dto.mentoring.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringMatchDetailCreateRequest {


    private String topic;

    private String summary;

    private LocalDateTime sessionDate;

    private LocalDateTime endDate;

    private Integer durationMinutes;

}

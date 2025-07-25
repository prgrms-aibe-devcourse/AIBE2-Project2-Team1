package com.example.campy.dto.mentoring.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringMatchDetailUpdateRequest {
    @NotBlank
    private String topic;

    private String summary;

    @NotNull
    private LocalDateTime sessionDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private Integer durationMinutes;
}

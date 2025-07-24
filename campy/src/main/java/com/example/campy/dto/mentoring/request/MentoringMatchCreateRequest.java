package com.example.campy.dto.mentoring.request;

import com.example.campy.constant.MentoringType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringMatchCreateRequest {

    @NotNull
    private Integer mentoringOfferId;

    @NotNull
    private MentoringType type;



}

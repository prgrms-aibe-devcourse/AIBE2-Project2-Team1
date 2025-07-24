package com.example.campy.dto.mentoring.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringMatchCreateCombinedRequest {

    @Valid
    @NotNull
    private MentoringMatchCreateRequest matchRequest;

    @Valid
    @NotNull
    private MentoringMatchDetailCreateRequest detailRequest;

}

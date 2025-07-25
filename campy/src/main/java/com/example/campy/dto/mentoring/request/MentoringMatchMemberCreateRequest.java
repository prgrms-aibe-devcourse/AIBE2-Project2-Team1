package com.example.campy.dto.mentoring.request;

import com.example.campy.constant.MatchRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentoringMatchMemberCreateRequest {

    @NotNull
    private Integer userId;

    @NotNull
    private MatchRole role;


}

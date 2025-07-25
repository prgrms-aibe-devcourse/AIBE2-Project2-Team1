package com.example.campy.dto.mentoring.request;

import com.example.campy.constant.MentoringStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MentoringMatchUpdateRequest {

    private MentoringStatus status;

}

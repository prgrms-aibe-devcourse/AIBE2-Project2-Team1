package com.example.campy.dto.mentoring.request;

import com.example.campy.constant.MentoringStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentoringMatchUpdateRequest {

    private MentoringStatus status;

}

package com.example.campy.dto.mentoring;

import com.example.campy.constant.MentoringStatus;
import lombok.Getter;

@Getter
public class MentoringOfferSearchCondition {

    private String keyword; // 멘토 이름, 설명, 해시 태그를 통합하여 검색
    private MentoringStatus status; // 상태 검색

}

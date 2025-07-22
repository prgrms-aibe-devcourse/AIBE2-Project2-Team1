package com.example.campy.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MentoringStatus {

    REQUESTED("요청됨"),
    PENDING("보류중"),
    ONGOING("진행중"),
    COMPLETED("완료"),
    CANCELLED("취소됨");

    private final String label;

}

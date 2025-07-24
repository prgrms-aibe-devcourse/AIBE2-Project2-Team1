package com.example.campy.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MentoringStatus {

    REQUESTED("요청중"),              // 1. 멘토링 제안 생성됨 (멘티가 요청)
    WAITING_FOR_MENTOR("멘토 수락 대기중"), // 2. 멘티가 수락 -> 멘토가 아직 수락하지 않음
    ONGOING("진행중"),                // 3. 멘토와 멘티 모두 수락 -> 진행 시작
    PENDING("보류중"),                // 4. 일정 등 이유로 잠시 중단됨
    COMPLETED("완료"),                // 5. 멘토링 완료
    CANCELED("취소됨");               // 6. 중간 취소 (멘토/멘티 어느 쪽이든)

    private final String label;

}

package com.example.campy.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MentoringType {

    INDIVIDUAL("개인"),
    GROUP("그룹");

    private final String label;

}

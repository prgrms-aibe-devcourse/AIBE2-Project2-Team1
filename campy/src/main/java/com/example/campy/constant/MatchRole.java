package com.example.campy.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchRole {
    MENTOR("멘토"), MENTEE("멘티");

    private final String label;
}

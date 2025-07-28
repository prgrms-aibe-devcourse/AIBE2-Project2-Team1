package com.example.campy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {
    private String to;           // 받는 사람 닉네임 (프론트 필드명과 동일하게)
    private String content;      // 메시지 내용
}
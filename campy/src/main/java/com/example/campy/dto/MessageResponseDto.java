package com.example.campy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MessageResponseDto {
    private String nickname;        // 보낸 사람 닉네임
    private String content;         // 메시지 내용
    private LocalDateTime sentAt;   // 보낸 시간
}
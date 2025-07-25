package com.example.campy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {
    private Integer receiverId;  // 받는 사람 userId
    private String content;      // 메시지 내용
}
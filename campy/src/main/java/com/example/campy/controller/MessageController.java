package com.example.campy.controller;

import com.example.campy.dto.MessageRequestDto;
import com.example.campy.dto.MessageResponseDto;
import com.example.campy.entity.Message;
import com.example.campy.entity.User;
import com.example.campy.repository.MessageRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.service.MessageService;
import com.example.campy.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    // 메시지 보내기
    @PostMapping
    public MessageResponseDto sendMessage(@RequestBody MessageRequestDto dto,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("로그인이 필요합니다!");
        }
        User sender = userDetails.getUser();
        User receiver = userRepository.findByNickname(dto.getTo())
                .orElseThrow(() -> new RuntimeException("받는 사람 없음"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(dto.getContent())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();

        Message saved = messageRepository.save(message);

        return new MessageResponseDto(
                saved.getSender().getNickname(),
                saved.getContent(),
                saved.getSentAt()
        );
    }

    // 받은 메시지 목록 (상대방: sender 닉네임)
    @GetMapping("/inbox")
    public List<MessageResponseDto> getInbox(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("로그인이 필요합니다!");
        }
        User user = userDetails.getUser();
        return messageRepository.findByReceiverOrderBySentAtDesc(user)
                .stream()
                .map(msg -> MessageResponseDto.builder()
                        .nickname(msg.getSender().getNickname())
                        .content(msg.getContent())
                        .sentAt(msg.getSentAt())
                        .build())
                .toList();
    }

    // 보낸 메시지 목록 (상대방: receiver 닉네임)
    @GetMapping("/sent")
    public List<MessageResponseDto> getSentMessages(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("로그인이 필요합니다!");
        }
        User user = userDetails.getUser();
        return messageRepository.findBySenderOrderBySentAtDesc(user)
                .stream()
                .map(msg -> MessageResponseDto.builder()
                        .nickname(msg.getReceiver().getNickname())
                        .content(msg.getContent())
                        .sentAt(msg.getSentAt())
                        .build())
                .toList();
    }

    // 쪽지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("로그인이 필요합니다!");
        }
        messageService.deleteMessage(messageId, userDetails.getUser());
        return ResponseEntity.ok().body("쪽지 삭제 완료");
    }
}
package com.example.campy.controller;

import com.example.campy.dto.MessageRequestDto;
import com.example.campy.dto.MessageResponseDto;
import com.example.campy.entity.Message;
import com.example.campy.entity.User;
import com.example.campy.jwt.JwtUtil;
import com.example.campy.repository.MessageRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final MessageService messageService;

    private User extractUserFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("토큰 누락");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("유효하지 않은 토큰");
        }
        Integer userId = jwtUtil.getUserId(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
    }

    // 메시지 보내기
    @PostMapping
    public MessageResponseDto sendMessage(@RequestBody MessageRequestDto dto, HttpServletRequest request) {
        User sender = extractUserFromRequest(request);
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("받는 사람 없음"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(dto.getContent())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();

        Message saved = messageRepository.save(message);

        // DTO로 응답 구성
        return new MessageResponseDto(
                saved.getSender().getNickname(),
                saved.getContent(),
                saved.getSentAt()
        );
    }

    // 받은 메시지 목록 (상대방: sender 닉네임)
    @GetMapping("/inbox")
    public List<MessageResponseDto> getInbox(HttpServletRequest request) {
        User user = extractUserFromRequest(request);
        return messageRepository.findByReceiverOrderBySentAtDesc(user)
                .stream()
                .map(msg -> MessageResponseDto.builder()
                        .nickname(msg.getSender().getNickname())  // 보낸 사람 닉네임
                        .content(msg.getContent())
                        .sentAt(msg.getSentAt())
                        .build())
                .toList();
    }

    // 보낸 메시지 목록 (상대방: receiver 닉네임)
    @GetMapping("/sent")
    public List<MessageResponseDto> getSentMessages(HttpServletRequest request) {
        User user = extractUserFromRequest(request);
        return messageRepository.findBySenderOrderBySentAtDesc(user)
                .stream()
                .map(msg -> MessageResponseDto.builder()
                        .nickname(msg.getReceiver().getNickname())  // 받는 사람 닉네임
                        .content(msg.getContent())
                        .sentAt(msg.getSentAt())
                        .build())
                .toList();
    }

    // 쪽지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId, HttpServletRequest request) {
        messageService.deleteMessage(messageId, request);
        return ResponseEntity.ok().body("쪽지 삭제 완료");
    }
}
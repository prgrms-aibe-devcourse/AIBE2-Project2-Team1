package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.dto.MessageRequestDto;
import com.example.campy.dto.MessageResponseDto;
import com.example.campy.entity.Message;
import com.example.campy.entity.User;
import com.example.campy.exception.GeneralException;
import com.example.campy.jwt.JwtUtil;
import com.example.campy.repository.MessageRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 🔹 토큰에서 userId 추출
    private Integer extractUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new GeneralException(ErrorCode.INVALID_TOKEN, "Authorization 헤더가 없거나 형식이 잘못되었습니다.");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new GeneralException(ErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다.");
        }
        return jwtUtil.getUserId(token);
    }

    public MessageResponseDto sendMessage(MessageRequestDto requestDto, HttpServletRequest request) {
        Integer senderId = extractUserId(request);
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
        User receiver = userRepository.findById(requestDto.getReceiverId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(requestDto.getContent())
                .isRead(false)
                .sentAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        return new MessageResponseDto(sender.getNickname(), message.getContent(), message.getSentAt());
    }

    public List<MessageResponseDto> getInbox(HttpServletRequest request) {
        Integer receiverId = extractUserId(request);
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        List<Message> messages = messageRepository.findByReceiverOrderBySentAtDesc(receiver);

        return messages.stream()
                .map(m -> new MessageResponseDto(m.getSender().getNickname(), m.getContent(), m.getSentAt()))
                .collect(Collectors.toList());
    }

    public void deleteMessage(Long messageId, HttpServletRequest request) {
        Integer userId = extractUserId(request);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MESSAGE_NOT_FOUND));

        if (!message.getSender().getUserId().equals(userId)
                && !message.getReceiver().getUserId().equals(userId)) {
            throw new GeneralException(ErrorCode.ACCESS_DENIED);
        }

        messageRepository.delete(message);
    }
}
package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.dto.MessageRequestDto;
import com.example.campy.dto.MessageResponseDto;
import com.example.campy.entity.Message;
import com.example.campy.entity.User;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MessageRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageResponseDto sendMessage(MessageRequestDto requestDto, User sender) {
        User receiver = userRepository.findByNickname(requestDto.getTo())
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

    public List<MessageResponseDto> getInbox(User receiver) {
        List<Message> messages = messageRepository.findByReceiverOrderBySentAtDesc(receiver);

        return messages.stream()
                .map(m -> new MessageResponseDto(m.getSender().getNickname(), m.getContent(), m.getSentAt()))
                .collect(Collectors.toList());
    }

    public List<MessageResponseDto> getSent(User sender) {
        List<Message> messages = messageRepository.findBySenderOrderBySentAtDesc(sender);

        return messages.stream()
                .map(m -> new MessageResponseDto(m.getReceiver().getNickname(), m.getContent(), m.getSentAt()))
                .collect(Collectors.toList());
    }

    public void deleteMessage(Long messageId, User user) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MESSAGE_NOT_FOUND));

        if (!message.getSender().getUserId().equals(user.getUserId())
                && !message.getReceiver().getUserId().equals(user.getUserId())) {
            throw new GeneralException(ErrorCode.ACCESS_DENIED);
        }

        messageRepository.delete(message);
    }
}
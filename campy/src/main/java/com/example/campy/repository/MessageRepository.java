package com.example.campy.repository;

import com.example.campy.entity.Message;
import com.example.campy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverOrderBySentAtDesc(User receiver);
    List<Message> findBySenderOrderBySentAtDesc(User sender);
}
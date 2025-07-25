package com.example.campy.repository;

import com.example.campy.entity.MessageRoom;
import com.example.campy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {
    Optional<MessageRoom> findBySenderAndReceiver(User sender, User receiver);
    List<MessageRoom> findBySenderOrReceiver(User sender, User receiver);
}
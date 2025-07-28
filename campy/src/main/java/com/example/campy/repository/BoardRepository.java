package com.example.campy.repository;

import com.example.campy.entity.Board;
import com.example.campy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByUser(User user);
}

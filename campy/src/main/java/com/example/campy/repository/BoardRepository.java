package com.example.campy.repository;

import com.example.campy.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // List import 추가

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByIsDeletedFalse(); // 추가된 메서드
}

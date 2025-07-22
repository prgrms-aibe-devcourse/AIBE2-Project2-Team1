package com.example.campy.repository;

import com.example.campy.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByContentContainingIgnoreCase(String content);
    List<Review> findByCategoryContainingIgnoreCase(String category);
    List<Review> findByAuthor_NicknameContainingIgnoreCase(String nickname);
    List<Review> findByTargetUser_NicknameContainingIgnoreCase(String nickname);
}

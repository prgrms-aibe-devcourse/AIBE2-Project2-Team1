package com.example.campy.repository;

import com.example.campy.entity.Review;
import com.example.campy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByDeletedAtIsNull();
    List<Review> findByContentContainingIgnoreCaseAndDeletedAtIsNull(String content);
    List<Review> findByCategoryContainingIgnoreCaseAndDeletedAtIsNull(String category);
    List<Review> findByAuthor_NicknameContainingIgnoreCaseAndDeletedAtIsNull(String nickname);
    List<Review> findByTargetUser_NicknameContainingIgnoreCaseAndDeletedAtIsNull(String nickname);
    List<Review> findByAuthorAndDeletedAtIsNull(User author);
    List<Review> findByTargetUserAndDeletedAtIsNull(User targetUser);
}

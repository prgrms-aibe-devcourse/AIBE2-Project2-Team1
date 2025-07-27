package com.example.campy.repository;

import com.example.campy.entity.Talent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.campy.entity.User;
import java.util.List;

public interface TalentRepository extends JpaRepository<Talent, Integer> {
    Page<Talent> findByDeletedFalse(Pageable pageable);

    Page<Talent> findByDeletedFalseAndStatus(String status, Pageable pageable);

    Page<Talent> findByDeletedFalseAndStatusAndCategory(String status, String category, Pageable pageable);

    Page<Talent> findByDeletedFalseAndCategory(String category, Pageable pageable);

    Page<Talent> findByTags_NameAndDeletedFalse(String tagName, Pageable pageable);


    Page<Talent> findByTags_NameAndCategoryAndDeletedFalse(String tagName, String category, Pageable pageable);

    List<Talent> findByUserAndDeletedFalse(User user);
}


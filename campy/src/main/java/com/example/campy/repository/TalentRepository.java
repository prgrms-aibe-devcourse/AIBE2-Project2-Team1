package com.example.campy.repository;

import com.example.campy.entity.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalentRepository extends JpaRepository<Talent, Integer> {
    List<Talent> findByIsDeletedFalseOrderByCreatedAtDesc();
}

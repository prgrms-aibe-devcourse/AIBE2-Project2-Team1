package com.example.campy.repository;

import com.example.campy.entity.Talent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalentRepository extends JpaRepository<Talent, Integer> {
    Page<Talent> findByIsDeletedFalse(Pageable pageable);

    Page<Talent> findByIsDeletedFalseAndStatus(String status, Pageable pageable);

    Page<Talent> findByIsDeletedFalseAndStatusAndCategory(String status, String category, Pageable pageable);

    Page<Talent> findByIsDeletedFalseAndCategory(String category, Pageable pageable);

    Page<Talent> findByTags_NameAndIsDeletedFalse(String tagName, Pageable pageable);

    Page<Talent> findByTags_NameAndCategoryAndIsDeletedFalse(String tagName, String category, Pageable pageable);

import org.springframework.data.jpa.repository.JpaRepository;

public interface TalentRepository extends JpaRepository<Talent, Integer> {

}

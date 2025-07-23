package com.example.campy.repository;

import com.example.campy.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Optional<Material> findById(Integer materialId);

    List<Material> findByIsDeletedFalseOrderByCreatedAtDesc();

    List<Material> findByIsDeletedFalseOrderByPriceAsc();

    List<Material> findByIsDeletedFalseOrderByPriceDesc();

    List<Material> findByTitleContainingIgnoreCase(String keyword);
}
package com.example.campy.repository;

import com.example.campy.entity.Material;
import com.example.campy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    List<Material> findBySellerAndIsDeletedFalse(User seller);
    List<Material> findByIsDeletedFalse();
}
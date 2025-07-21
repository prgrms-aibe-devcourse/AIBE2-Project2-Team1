package com.example.campy.repository;

import com.example.campy.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByLevel(String level);
    Optional<Admin> findByUsername(String username);
}
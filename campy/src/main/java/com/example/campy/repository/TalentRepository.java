package com.example.campy.repository;

import com.example.campy.entity.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalentRepository extends JpaRepository<Talent, Integer> {
}

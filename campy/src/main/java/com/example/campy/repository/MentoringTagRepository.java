package com.example.campy.repository;

import com.example.campy.entity.MentoringTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentoringTagRepository extends JpaRepository<MentoringTag, Integer> {

    Optional<MentoringTag> findByName(String name);

}

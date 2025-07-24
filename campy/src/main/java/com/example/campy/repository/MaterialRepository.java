package com.example.campy.repository;

import com.example.campy.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Optional<Material> findById(Integer materialId);

    // 키워드(제목) 검색 + 삭제되지 않은 자료만
    Page<Material> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);

    Optional<Material> findByMaterialIdAndIsDeletedFalse(Integer materialId);

    // 내가 등록한 자료 조회
    Page<Material> findBySeller_UserIdAndIsDeletedFalse(Integer userId, Pageable pageable);

    // 전체 자료 조회 (삭제 안 된 것만, 페이징)
    Page<Material> findByIsDeletedFalse(Pageable pageable);
}
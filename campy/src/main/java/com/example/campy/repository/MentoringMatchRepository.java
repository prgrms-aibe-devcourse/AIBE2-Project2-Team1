package com.example.campy.repository;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.entity.MentoringMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentoringMatchRepository extends JpaRepository<MentoringMatch, Integer> {

    Page<MentoringMatch> findByStatus(MentoringStatus status, Pageable pageable);

    // 상태가 DELETED가 아닌 매칭 전체 조회
    Page<MentoringMatch> findByStatusNot(MentoringStatus status, Pageable pageable);

    // 상태가 DELETED가 아닌 특정 상태의 매칭 조회
    Page<MentoringMatch> findByStatusAndStatusNot(MentoringStatus status, MentoringStatus excludedStatus, Pageable pageable);

    // ID로 조회할 때도 DELETED 상태 제외
    Optional<MentoringMatch> findByIdAndStatusNot(Integer matchId, MentoringStatus status);

}

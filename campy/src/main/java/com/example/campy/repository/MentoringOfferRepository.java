package com.example.campy.repository;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.entity.MentoringOffer ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentoringOfferRepository extends JpaRepository<MentoringOffer, Integer> , MentoringOfferRepositoryCustom{

    // 삭제되지 않은 멘토링 요청 조회
    Page<MentoringOffer> findByIsDeletedFalse(Pageable pageable);

    // 특정 유저의 요청 조회
    List<MentoringOffer> findByUser_UserIdAndIsDeletedFalse(Integer userId);

    // 상태별 조회
    Page<MentoringOffer> findByStatusAndIsDeletedFalse(MentoringStatus status, Pageable pageable);

}

package com.example.campy.repository;

import com.example.campy.entity.MentoringOffer ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentoringOfferRepository extends JpaRepository<MentoringOffer, Integer> {

    // 삭제되지 않은 멘토링 요청 조회
    List<MentoringOffer> findByIsDeletedFalse();

    // 특정 유저의 요청 조회
    List<MentoringOffer> findByUserId(Integer userId);

}

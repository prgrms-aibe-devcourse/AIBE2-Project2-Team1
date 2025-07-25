package com.example.campy.repository;

import com.example.campy.dto.mentoring.MentoringOfferSearchCondition;
import com.example.campy.entity.MentoringOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MentoringOfferRepositoryCustom {

    // 검색 로직 초안, 추후 삭제 예정
    Page<MentoringOfferRepository> search(MentoringOfferSearchCondition condition, Pageable pageable);

    Page<MentoringOffer> searchWithKeyword(String keyword, Pageable pageable);

    Page<MentoringOffer> searchByTag(String tagKeyword, Pageable pageable);
}

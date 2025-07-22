package com.example.campy.repository;

import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.MentoringTagPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MentoringTagPostRepository extends JpaRepository<MentoringTagPost, Integer> {

    List<MentoringTagPost> findByMentoringOffer(MentoringOffer offer);

    @Query("SELECT mtp.tag.name FROM MentoringTagPost mtp WHERE mtp.mentoringOffer.offerId = :offerId")
    List<String> findTagNamesByOfferId(@Param("offerId") Integer offerId);

    void deleteByMentoringOffer(MentoringOffer offer);
}

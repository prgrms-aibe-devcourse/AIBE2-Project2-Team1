package com.example.campy.service;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.repository.MentoringOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MentoringOfferService {

    private final MentoringOfferRepository mentoringOfferRepository;

    public MentoringOfferResponse create(MentoringOfferCreateRequest req){

        MentoringOffer entity = req.toEntity(); // DTO -> Entity
        MentoringOffer saved = mentoringOfferRepository.save(entity);

        return MentoringOfferResponse.from(saved); // Entity -> Response DTO
    }

}

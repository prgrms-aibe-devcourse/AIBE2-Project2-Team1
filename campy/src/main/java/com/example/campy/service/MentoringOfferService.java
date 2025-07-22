package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MentoringOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringOfferService {

    private final MentoringOfferRepository mentoringOfferRepository;

    public MentoringOfferResponse create(MentoringOfferCreateRequest req){

        MentoringOffer entity = req.toEntity(); // DTO -> Entity
        MentoringOffer saved = mentoringOfferRepository.save(entity);

        return MentoringOfferResponse.from(saved); // Entity -> Response DTO
    }

    public List<MentoringOfferResponse> findAll(){
        return mentoringOfferRepository.findByIsDeletedFalse()
                .stream()
                .map(MentoringOfferResponse::from)
                .toList();
    }

    public MentoringOfferResponse findById(Integer offerId){
        MentoringOffer offer = mentoringOfferRepository.findById(offerId)
                .filter(o -> !o.getIsDeleted())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "해당 제안이 존재하지 않거나 삭제되었습니다."));

        return MentoringOfferResponse.from(offer);
    }

    public List<MentoringOfferResponse> findByUserId(Integer userId){
        return mentoringOfferRepository.findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(MentoringOfferResponse::from)
                .toList();
    }

    @Transactional
    public MentoringOfferResponse update(Integer offerId, MentoringOfferUpdateRequest  req){
        MentoringOffer offer = mentoringOfferRepository.findById(offerId)
                .filter(o -> !o.getIsDeleted())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        offer.setTitle(req.getTitle());
        offer.setDescription(req.getDescription());
        offer.setPrice(req.getPrice());
        offer.setLocation(req.getLocation());
        offer.setMaxParticipants(req.getMaxParticipants());
        offer.setDuration(req.getDuration());
        offer.setStatus(req.getStatus());
        offer.setUpdatedAt(LocalDateTime.now());

        return MentoringOfferResponse.from(offer);
    }

    @Transactional
    public void delete(Integer offerId){
        MentoringOffer offer = mentoringOfferRepository.findById(offerId)
                .filter(o -> !o.getIsDeleted())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        offer.setIsDeleted(true);
        offer.setUpdatedAt(LocalDateTime.now());

    }

}

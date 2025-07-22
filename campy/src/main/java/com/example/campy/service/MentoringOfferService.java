package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MentoringOfferRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringOfferService {

    private final MentoringOfferRepository mentoringOfferRepository;
    private final UserRepository userRepository;

    public MentoringOfferResponse create(MentoringOfferCreateRequest req, Integer userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        MentoringOffer entity = req.toEntity(user); // User 객체 전달
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
        return mentoringOfferRepository.findByUser_UserIdAndIsDeletedFalse(userId)
                .stream()
                .map(MentoringOfferResponse::from)
                .toList();
    }

}

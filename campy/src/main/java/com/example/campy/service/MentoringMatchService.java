package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringMatchCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MentoringMatchRepository;
import com.example.campy.repository.MentoringOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MentoringMatchService {

    private final MentoringMatchRepository matchRepo;
    private final MentoringOfferRepository offerRepo;

    @Transactional
    public MentoringMatchResponse createMatch(MentoringMatchCreateRequest req){
        MentoringOffer offer = offerRepo.findById(req.getMentoringOfferId())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND,"해당 멘토링이 존재하지 않습니다."));

        MentoringMatch match = MentoringMatch.builder()
                .mentoringOffer(offer)
                .status(MentoringStatus.WAITING_FOR_MENTOR)
                .type(req.getType())
                .createdAt(LocalDateTime.now())
                .build();

        matchRepo.save(match);

        // 넘겨주는 정보는 프론트랑 맞춰봐야함
        return MentoringMatchResponse.builder()
                .matchId(match.getMatchId())
                .mentoringOfferId(match.getMentoringOffer().getOfferId())
                .status(match.getStatus().getLabel())
                .type(match.getType().getLabel())
                .createdAt(match.getCreatedAt())
                .build();
    }


}

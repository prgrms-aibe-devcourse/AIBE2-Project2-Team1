package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringMatchCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringMatchUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MentoringMatchRepository;
import com.example.campy.repository.MentoringOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 전체 매칭 조회 + 페이징
    public Page<MentoringMatchResponse> findAll(Pageable pageable) {
        Page<MentoringMatch> page = matchRepo.findByStatusNot(MentoringStatus.DELETED, pageable);
        return toResponse(page);
    }

    // 매칭  ID 조회
    public MentoringMatchResponse findById(Integer matchId) {
        MentoringMatch match = matchRepo.findByIdAndStatusNot(matchId, MentoringStatus.DELETED)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "매칭이 존재하지 않거나 삭제되었습니다."));
        return toResponse(match);
    }

    // 상태별 조회 (DELETED 제외) + 페이징
    public Page<MentoringMatchResponse> findByStatus(MentoringStatus status, Pageable pageable) {
        Page<MentoringMatch> page = matchRepo.findByStatusAndStatusNot(status, MentoringStatus.DELETED, pageable);
        return toResponse(page);
    }

    // 공통 응답 변환
    private MentoringMatchResponse toResponse(MentoringMatch match) {
        return MentoringMatchResponse.builder()
                .matchId(match.getMatchId())
                .mentoringOfferId(match.getMentoringOffer().getOfferId())
                .status(match.getStatus().getLabel())
                .type(match.getType().getLabel())
                .createdAt(match.getCreatedAt())
                .build();
    }

    // 공통 응답 변환 + 페이징
    private Page<MentoringMatchResponse> toResponse(Page<MentoringMatch> page) {
        return page.map(this::toResponse); // MentoringMatch → MentoringMatchResponse 변환
    }

    // 매칭 상태 업데이트
    @Transactional
    public void updatematch(Integer matchId, MentoringMatchUpdateRequest req){
        MentoringMatch match = matchRepo.findById(matchId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "해당 멘토링이 존재하지 않습니다."));

        match.setStatus(req.getStatus());
        match.setUpdatedAt(LocalDateTime.now());
    }

    // 매칭 삭제 (업데이트 시간은 그대로 둠)
    @Transactional
    public void deleteMatch(Integer matchId){
        MentoringMatch match = matchRepo.findById(matchId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "해당 매칭이 존재하지 않습니다."));

        match.setStatus(MentoringStatus.DELETED);
        match.setDeletedAt(LocalDateTime.now());
    }

    // 중복되는 throw 부분 빼둔것 추후 사용
    private MentoringMatch getMatchOrThrow(Integer matchId){
        return matchRepo.findByIdAndStatusNot(matchId, MentoringStatus.DELETED)
                .orElseThrow(() ->
                        new GeneralException(ErrorCode.NOT_FOUND, "해당 매칭이 존재하지 않거나 삭제되었습니다."));
    }


}

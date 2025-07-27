package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.*;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringMatchService {

    private final MentoringMatchRepository matchRepo;
    private final MentoringOfferRepository offerRepo;
    private final MentoringMatchDetailService detailService;
    private final MentoringMatchMemberService memberService;

    // detail 없이 match만 생성

    public MentoringMatchResponse createMatch(MentoringMatchCreateRequest req) {
        MentoringOffer offer = offerRepo.findById(req.getMentoringOfferId())
                .filter(o -> o.getStatus() != MentoringStatus.DELETED)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "해당 멘토링이 존재하지 않습니다."));

        // 매칭 생성
        MentoringMatch match = MentoringMatch.builder()
                .mentoringOffer(offer)
                .status(MentoringStatus.WAITING_FOR_MENTOR)
                .type(req.getType())
                .createdAt(LocalDateTime.now())
                .build();

        matchRepo.save(match);

        return MentoringMatchResponse.builder()
                .matchId(match.getMatchId())
                .mentoringOfferId(match.getMentoringOffer().getOfferId())
                .status(match.getStatus().getLabel())
                .type(match.getType().getLabel())
                .createdAt(match.getCreatedAt())
                .build();
    }

    // member와 match만 생성
    public MentoringMatchResponse createMemberMatch(MentoringMatchCreateCombinedRequest req) {
        MentoringMatchCreateRequest matchReq = req.getMatchRequest();
        List<MentoringMatchMemberCreateRequest> memberReqs = req.getMembers();

        MentoringOffer offer = offerRepo.findById(matchReq.getMentoringOfferId())
                .filter(o -> o.getStatus() != MentoringStatus.DELETED)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "해당 멘토링이 존재하지 않습니다."));

        // 매칭 생성
        MentoringMatch match = MentoringMatch.builder()
                .mentoringOffer(offer)
                .status(MentoringStatus.WAITING_FOR_MENTOR)
                .type(matchReq.getType())
                .createdAt(LocalDateTime.now())
                .build();

        matchRepo.save(match);

        memberService.createMembers(match, memberReqs);

        return MentoringMatchResponse.builder()
                .matchId(match.getMatchId())
                .mentoringOfferId(offer.getOfferId())
                .status(match.getStatus().getLabel())
                .type(match.getType().getLabel())
                .createdAt(match.getCreatedAt())
                .build();
    }

    // detail, member 포함한 match 생성
    @Transactional
    public MentoringMatchResponse createMatchWithDetail(MentoringMatchCreateCombinedRequest req){
        MentoringMatchCreateRequest matchReq = req.getMatchRequest();
        MentoringMatchDetailCreateRequest detailReq = req.getDetailRequest();
        List<MentoringMatchMemberCreateRequest> memberReqs = req.getMembers();

        MentoringOffer offer = offerRepo.findById(matchReq.getMentoringOfferId())
                .filter(o -> o.getStatus() != MentoringStatus.DELETED)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "해당 멘토링이 존재하지 않습니다."));

        // 매칭 생성
        MentoringMatch match = MentoringMatch.builder()
                .mentoringOffer(offer)
                .status(MentoringStatus.WAITING_FOR_MENTOR)
                .type(matchReq.getType())
                .createdAt(LocalDateTime.now())
                .build();

        matchRepo.save(match);

        detailService.createDetail(match, detailReq);
        memberService.createMembers(match, memberReqs);

        return MentoringMatchResponse.builder()
                .matchId(match.getMatchId())
                .mentoringOfferId(offer.getOfferId())
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
        MentoringMatch match = matchRepo.findByMatchIdAndStatusNot(matchId, MentoringStatus.DELETED)
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
        return matchRepo.findByMatchIdAndStatusNot(matchId, MentoringStatus.DELETED)
                .orElseThrow(() ->
                        new GeneralException(ErrorCode.NOT_FOUND, "해당 매칭이 존재하지 않거나 삭제되었습니다."));
    }


}

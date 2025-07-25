package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.dto.mentoring.request.MentoringMatchDetailCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringMatchDetailUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchDetailResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringMatchDetail;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MentoringMatchDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MentoringMatchDetailService {

    private final MentoringMatchDetailRepository detailRepo;

    public void createDetail(MentoringMatch match, MentoringMatchDetailCreateRequest req){
        MentoringMatchDetail detail = MentoringMatchDetail.builder()
                .mentoringMatch(match)
                .topic(req.getTopic())
                .summary(req.getSummary())
                .sessionDate(req.getSessionDate())
                .endDate(req.getEndDate())
                .durationMinutes(req.getDurationMinutes())
                .createdAt(LocalDateTime.now())
                .build();

        detailRepo.save(detail);
    }

    public MentoringMatchDetailResponse findById(Integer id){
        MentoringMatchDetail detail = detailRepo.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "매칭 상세 정보를 찾을 수 없습니다."));

        return MentoringMatchDetailResponse.from(detail);
    }

    @Transactional
    public MentoringMatchDetailResponse update(Integer id, MentoringMatchDetailUpdateRequest req){
        MentoringMatchDetail detail = detailRepo.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "매칭 상세 정보를 찾을 수 없습니다."));

        detail.setTopic(req.getTopic());
        detail.setSummary(req.getSummary());
        detail.setSessionDate(req.getSessionDate());
        detail.setEndDate(req.getEndDate());
        detail.setDurationMinutes(req.getDurationMinutes());

        return MentoringMatchDetailResponse.from(detail);

    }

}

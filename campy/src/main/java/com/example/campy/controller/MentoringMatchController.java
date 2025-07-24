package com.example.campy.controller;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringMatchCreateCombinedRequest;
import com.example.campy.dto.mentoring.request.MentoringMatchCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringMatchUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchResponse;
import com.example.campy.service.MentoringMatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MentoringMatchController {

    private final MentoringMatchService matchService;

    @PostMapping
    public ResponseEntity<MentoringMatchResponse> createMatch(@RequestBody @Valid MentoringMatchCreateCombinedRequest req){

        MentoringMatchResponse res = matchService.createMatchWithDetail(req.getMatchRequest(), req.getDetailRequest());

        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }

    // 전체 매칭 조회 + 페이징, 나중에 admin만 보게 설정 필요
    @GetMapping
    public ResponseEntity<Page<MentoringMatchResponse>> getAllMatches(Pageable pageable){
        return ResponseEntity.ok(matchService.findAll(pageable));
    }

    // 매칭 ID 조회
    @GetMapping("/{matchId}")
    public ResponseEntity<MentoringMatchResponse> getMatchById(@PathVariable Integer matchId){
        return ResponseEntity.ok(matchService.findById(matchId));
    }


    // 상태별 조회 + 페이징
    @GetMapping("/status")
    public ResponseEntity<Page<MentoringMatchResponse>> getMatchesByStatus(
            @RequestParam MentoringStatus status,
            Pageable pageable
            ){
        return ResponseEntity.ok(matchService.findByStatus(status,pageable));
    }

    // 매칭 상태 업데이트
    @PatchMapping("/{matchId}")
    public ResponseEntity<Void> updateMentoringMatch(
            @PathVariable Integer matchId,
            @RequestBody MentoringMatchUpdateRequest req
            ){
        matchService.updatematch(matchId, req);

        return ResponseEntity.noContent().build();
    }

    // 매칭 삭제 ( status : DELETED )
    public ResponseEntity<Void> deleteMentoringMatch(@PathVariable Integer matchId){
        matchService.deleteMatch(matchId);
        return ResponseEntity.noContent().build();
    }

}

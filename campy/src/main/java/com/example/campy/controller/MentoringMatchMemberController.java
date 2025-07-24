package com.example.campy.controller;

import com.example.campy.constant.MatchRole;
import com.example.campy.dto.mentoring.response.MentoringMatchMemberResponse;
import com.example.campy.service.MentoringMatchMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match-members")
@RequiredArgsConstructor
public class MentoringMatchMemberController {

    private final MentoringMatchMemberService memberService;

    // 전체 조회
    // TODO: 페이징 추가하기
    @GetMapping
    public ResponseEntity<List<MentoringMatchMemberResponse>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    // 매칭 ID별 조회
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<MentoringMatchMemberResponse>> findByMatchId(@PathVariable Integer matchId) {
        return ResponseEntity.ok(memberService.findByMatchId(matchId));
    }

    // 사용자 ID별 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MentoringMatchMemberResponse>> findByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(memberService.findByUserId(userId));
    }

    // 매칭 ID + 역할(role) 조건 조회
    @GetMapping("/match/{matchId}/role")
    public ResponseEntity<List<MentoringMatchMemberResponse>> findByMatchIdAndRole(
            @PathVariable Integer matchId,
            @RequestParam MatchRole role) {
        return ResponseEntity.ok(memberService.findByMatchIdAndRole(matchId, role));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> delete(@PathVariable Integer memberId) {
        memberService.delete(memberId);
        return ResponseEntity.noContent().build();
    }


}

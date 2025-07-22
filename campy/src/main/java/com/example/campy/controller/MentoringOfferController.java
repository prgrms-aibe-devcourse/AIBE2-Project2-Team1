package com.example.campy.controller;

import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.service.MentoringOfferService;
import com.example.campy.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mentoring-offers")
@RequiredArgsConstructor
public class MentoringOfferController {

    private final MentoringOfferService mentoringOfferService;

    // 멘토링 제안 등록
    @PostMapping
    public ResponseEntity<MentoringOfferResponse> createMentoringOffer(
            @RequestBody @Valid MentoringOfferCreateRequest req,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){

        Integer userId = customUserDetails.getUserId();

        MentoringOfferResponse res = mentoringOfferService.create(req, userId);

        return ResponseEntity.ok(res);

    }

    // 모든 멘토링 제안 조회
    @GetMapping
    public ResponseEntity<List<MentoringOfferResponse>> getAllMentoringOffers() {
        return ResponseEntity.ok(mentoringOfferService.findAll());
    }

    // 제안 id별 검색
    @GetMapping("/{offerId}")
    public ResponseEntity<MentoringOfferResponse> getMentoringOfferById(@PathVariable Integer offerId) {
        return ResponseEntity.ok(mentoringOfferService.findById(offerId));
    }

    // 유저가 작성한 모든 멘토링 제안 검색
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MentoringOfferResponse>> getMentoringOffersByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(mentoringOfferService.findByUserId(userId));
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<MentoringOfferResponse> updateMentoringOffer(
            @PathVariable Integer offerId,
            @RequestBody @Valid MentoringOfferUpdateRequest req
            ) {
        return ResponseEntity.ok(mentoringOfferService.update(offerId, req));
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteMentoringOffer(@PathVariable Integer offerId) {
        mentoringOfferService.delete(offerId);

        return ResponseEntity.noContent().build(); // 204 No Content
    }
}

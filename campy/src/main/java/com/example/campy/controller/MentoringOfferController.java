package com.example.campy.controller;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.service.MentoringOfferService;
import com.example.campy.service.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<MentoringOfferResponse>> getAllMentoringOffers(Pageable pageable) {
        System.out.println("📌 page=" + pageable.getPageNumber() + ", size=" + pageable.getPageSize());

        return ResponseEntity.ok(mentoringOfferService.findAll(pageable));
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

    // 키워드 검색
    @GetMapping("/search")
    public ResponseEntity<Page<MentoringOfferResponse>> searchMentoringOffers(
            @RequestParam(required = false) String keyword, Pageable pageable
    ){
        return ResponseEntity.ok(mentoringOfferService.searchOffers(keyword,pageable));
    }

    // 해시 태그 검색
    @GetMapping("/search/by-tag")
    public ResponseEntity<Page<MentoringOfferResponse>> searchByTag(
            @RequestParam String tag,
            Pageable pageable
    ){
        return ResponseEntity.ok(mentoringOfferService.searchByTag(tag, pageable));
    }

    // 상태별 조회
    @GetMapping("/status")
    public ResponseEntity<Page<MentoringOfferResponse>> getOffersByStatus(
            @RequestParam MentoringStatus status,
            Pageable pageable
            ){
        return ResponseEntity.ok(mentoringOfferService.findByStatus(status, pageable));

    }
}

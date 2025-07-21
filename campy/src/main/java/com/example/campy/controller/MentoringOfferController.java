package com.example.campy.controller;

import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.service.MentoringOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @RequestBody @Valid MentoringOfferCreateRequest req
            ){

        MentoringOfferResponse res = mentoringOfferService.create(req);

        return ResponseEntity.ok(res);

    }

    @GetMapping
    public ResponseEntity<List<MentoringOfferResponse>> getAllMentoringOffers() {
        return ResponseEntity.ok(mentoringOfferService.findAll());
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<MentoringOfferResponse> getMentoringOfferById(@PathVariable Integer offerId) {
        return ResponseEntity.ok(mentoringOfferService.findById(offerId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MentoringOfferResponse>> getMentoringOffersByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(mentoringOfferService.findByUserId(userId));
    }
}

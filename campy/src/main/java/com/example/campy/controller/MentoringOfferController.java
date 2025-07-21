package com.example.campy.controller;

import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.service.MentoringOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

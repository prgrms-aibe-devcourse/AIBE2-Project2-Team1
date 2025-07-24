package com.example.campy.controller;

import com.example.campy.dto.mentoring.request.MentoringMatchCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchResponse;
import com.example.campy.service.MentoringMatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MentoringMatchController {

    private final MentoringMatchService matchService;

    @PostMapping
    public ResponseEntity<MentoringMatchResponse> createMatch(@RequestBody @Valid MentoringMatchCreateRequest req){

        MentoringMatchResponse res = matchService.createMatch(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }

}

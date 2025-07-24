package com.example.campy.controller;

import com.example.campy.dto.mentoring.response.MentoringMatchDetailResponse;
import com.example.campy.service.MentoringMatchDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match-details")
@RequiredArgsConstructor
public class MentoringMatchDetailController {

   private final MentoringMatchDetailService detailService;

    @GetMapping("/{id}")
    private ResponseEntity<MentoringMatchDetailResponse> getDetail(@PathVariable Integer id){
        MentoringMatchDetailResponse res = detailService.findById(id);

        return ResponseEntity.ok(res);
    }

}

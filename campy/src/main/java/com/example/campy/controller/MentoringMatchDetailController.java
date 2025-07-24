package com.example.campy.controller;

import com.example.campy.dto.mentoring.request.MentoringMatchDetailUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchDetailResponse;
import com.example.campy.entity.MentoringMatchDetail;
import com.example.campy.service.MentoringMatchDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match-details")
@RequiredArgsConstructor
public class MentoringMatchDetailController {

   private final MentoringMatchDetailService detailService;

    @GetMapping("/{id}")
    public ResponseEntity<MentoringMatchDetailResponse> getDetail(@PathVariable Integer id){
        MentoringMatchDetailResponse res = detailService.findById(id);

        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MentoringMatchDetailResponse> updateDetail(
            @PathVariable Integer id,
            @RequestBody @Valid MentoringMatchDetailUpdateRequest req
    ) {
        MentoringMatchDetailResponse res = detailService.update(id, req);
        return ResponseEntity.ok(res);
    }


}

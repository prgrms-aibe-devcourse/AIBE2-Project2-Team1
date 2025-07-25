package com.example.campy.controller;

import com.example.campy.dto.talent.request.TalentCreateRequest;
import com.example.campy.dto.talent.response.TalentResponseDto;
import com.example.campy.dto.talent.request.TalentUpdateRequest;
import com.example.campy.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talents")
public class TalentController {

    private final TalentService talentService;

    // 전체 조회 (정렬, 필터링 포함)
    @GetMapping
    public ResponseEntity<Page<TalentResponseDto>> getAllTalents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TalentResponseDto> talents = talentService.getAllTalents(pageable, status, category, tag);

        return ResponseEntity.ok(talents);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<TalentResponseDto> getTalentById(@PathVariable Integer id) {
        TalentResponseDto talent = talentService.getTalentById(id);
        // isDeleted 필드는 TalentResponseDto에 없으므로, 서비스 계층에서 처리하거나 별도의 로직 필요
        // 여기서는 단순히 DTO를 반환
        return ResponseEntity.ok(talent);
    }

    // 태그로 검색
    @GetMapping("/search")
    public ResponseEntity<Page<TalentResponseDto>> searchByTag(
            @RequestParam String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TalentResponseDto> talents = talentService.getAllTalents(pageable, null, null, tag);
        return ResponseEntity.ok(talents);
    }

    // 등록 + 이미지 + 태그 처리
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<TalentResponseDto> registerTalent(
            @RequestPart("data") TalentCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication) throws IOException {

        TalentResponseDto registeredTalent = talentService.createTalent(request, image, authentication);
        return ResponseEntity.ok(registeredTalent);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<TalentResponseDto> updateTalent(@PathVariable Integer id, @RequestPart("data") TalentUpdateRequest request, @RequestPart(value = "image", required = false) MultipartFile image, Authentication authentication) throws IOException {
        TalentResponseDto updatedTalent = talentService.updateTalent(id, request, image, authentication);
        return ResponseEntity.ok(updatedTalent);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTalent(@PathVariable Integer id, Authentication authentication) {
        talentService.deleteTalent(id, authentication);
        return ResponseEntity.noContent().build();
    }
}

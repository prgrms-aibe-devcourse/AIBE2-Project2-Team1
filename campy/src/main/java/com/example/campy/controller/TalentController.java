package com.example.campy.controller;

import com.example.campy.dto.TalentRequestDto;
import com.example.campy.entity.Tag;
import com.example.campy.entity.Talent;
import com.example.campy.entity.User;
import com.example.campy.repository.TagRepository;
import com.example.campy.repository.TalentRepository;
import com.example.campy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.campy.service.TalentService;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talents")
public class TalentController {

    private final TalentService talentService;

    // 전체 조회 (정렬, 필터링 포함)
    @GetMapping
    public ResponseEntity<Page<Talent>> getAllTalents(
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
        Page<Talent> talents = talentService.getAllTalents(pageable, status, category, tag);

        return ResponseEntity.ok(talents);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Talent> getTalentById(@PathVariable Integer id) {
        Talent talent = talentService.getTalentById(id);
        if (Boolean.TRUE.equals(talent.getIsDeleted())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(talent);
    }

    // 태그로 검색
    @GetMapping("/search")
    public ResponseEntity<Page<Talent>> searchByTag(
            @RequestParam String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Talent> talents = talentService.getAllTalents(pageable, null, null, tag);
        return ResponseEntity.ok(talents);
    }

    // 등록 + 이미지 + 태그 처리
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Talent> registerTalent(
            @RequestPart("data") TalentRequestDto request,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        Talent registeredTalent = talentService.registerTalent(request, image);
        return ResponseEntity.ok(registeredTalent);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Talent> updateTalent(@PathVariable Integer id, @RequestBody TalentRequestDto request, @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        Talent updatedTalent = talentService.updateTalent(id, request, image);
        return ResponseEntity.ok(updatedTalent);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTalent(@PathVariable Integer id) {
        Integer userId = 1; // JWT 사용 시 교체
        try {
            talentService.deleteTalent(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
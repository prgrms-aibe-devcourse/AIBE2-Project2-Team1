package com.example.campy.controller;

import com.example.campy.dto.TalentRequestDto;
import com.example.campy.entity.Talent;
import com.example.campy.repository.TalentRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talents")
public class TalentController {

    private final TalentRepository talentRepository;
    private final com.example.campy.jwt.JwtUtil jwtUtil;

    @Value("${upload.path}")
    private String uploadPath;

    // 전체 조회
    @GetMapping
    public List<Talent> getAllTalents() {
        return talentRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Talent> getTalentById(@PathVariable Integer id) {
        return talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getIsDeleted()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 등록 + 이미지 업로드
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Talent> registerTalent(
            @RequestPart("data") TalentRequestDto request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest servletRequest) throws IOException {

        // JWT에서 userId 추출
//        String token = resolveToken(servletRequest);
//        Claims claims = jwtUtil.extractClaims(token);
//        Integer userId = claims.get("userId", Integer.class);
        Integer userId = 1;


        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
            File dest = new File(uploadPath, fileName);
            image.transferTo(dest);
            imagePath = dest.getAbsolutePath(); // 필요 시 DB 저장 필드에 연결
        }

        Talent talent = Talent.builder()
                .userId(userId)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .availableDays(request.getAvailableDays())
                .offlineLocation(request.getOfflineLocation())
                .status("요청중")
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .imagePath(imagePath)
                .build();

        // 이미지 경로도 저장하고 싶으면 Talent에 필드 추가 후 setImagePath(imagePath);

        return ResponseEntity.ok(talentRepository.save(talent));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Talent> updateTalent(@PathVariable Integer id, @RequestBody TalentRequestDto request) {
        return talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getIsDeleted()))
                .map(talent -> {
                    talent.setTitle(request.getTitle());
                    talent.setDescription(request.getDescription());
                    talent.setPrice(request.getPrice());
                    talent.setAvailableDays(request.getAvailableDays());
                    talent.setOfflineLocation(request.getOfflineLocation());
                    talent.setUpdatedAt(LocalDateTime.now());
                    return ResponseEntity.ok(talentRepository.save(talent));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTalent(@PathVariable Integer id) {
        return talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getIsDeleted()))
                .map(talent -> {
                    talent.setIsDeleted(true);
                    talent.setUpdatedAt(LocalDateTime.now());
                    talentRepository.save(talent);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
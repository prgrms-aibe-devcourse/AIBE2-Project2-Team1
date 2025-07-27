package com.example.campy.controller;

import com.example.campy.dto.material.MaterialListDto;
import com.example.campy.dto.material.request.MaterialRequestDto;
import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    //자료 등록
    @PostMapping
    public ResponseEntity<MaterialResponseDto> createMaterial(
            @RequestBody MaterialRequestDto requestDto,
            Authentication authentication
    ) {
        String username = authentication.getName();
        MaterialResponseDto response = materialService.createMaterial(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 자료 목록 조회 (정렬 + 검색 포함)
    @GetMapping
    public ResponseEntity<Page<MaterialListDto>> getAllMaterials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false) String keyword
    ) {
        Page<MaterialListDto> materials = materialService.getAllMaterials(page, size, sort, keyword);
        return ResponseEntity.ok(materials);
    }

    //자료 삭제
    @DeleteMapping("/{materialId}")
    public ResponseEntity<String> deleteMaterial(
            @PathVariable Integer materialId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        materialService.deleteMaterial(materialId, username);
        return ResponseEntity.ok("자료가 삭제되었습니다.");
    }

    //자료 수정
    @PutMapping("/{materialId}")
    public ResponseEntity<MaterialResponseDto> updateMaterial(
            @PathVariable Integer materialId,
            @RequestBody MaterialRequestDto updateDto,
            Authentication authentication
    ) {
        String username = authentication.getName();
        MaterialResponseDto updatedMaterialDto = materialService.updateMaterial(materialId, username, updateDto);
        return ResponseEntity.ok().body(updatedMaterialDto);
    }

    //자료 상세 조회
    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialResponseDto> getMaterialById(@PathVariable Integer materialId) {
        MaterialResponseDto responseDto = materialService.getMaterialById(materialId);
        return ResponseEntity.ok(responseDto);
    }

    //자료 키워드 검색 API (단순 검색)
    @GetMapping("/search")
    public ResponseEntity<Page<MaterialListDto>> searchMaterials(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<MaterialListDto> materials = materialService.searchMaterials(keyword, pageable);
        return ResponseEntity.ok(materials);
    }

    //자료 파일 다운로드 API (결제한 사용자만 가능)
    @GetMapping("/{materialId}/download")
    public ResponseEntity<Resource> downloadMaterial(
            @PathVariable Integer materialId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return materialService.downloadMaterialFile(materialId, username);
    }

    //내가 등록한 자료 목록 조회
    @GetMapping("/my")
    public ResponseEntity<Page<MaterialListDto>> getMyMaterials(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String username = authentication.getName();

        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<MaterialListDto> materials = materialService.getMyMaterials(username, pageable);
        return ResponseEntity.ok(materials);
    }
}
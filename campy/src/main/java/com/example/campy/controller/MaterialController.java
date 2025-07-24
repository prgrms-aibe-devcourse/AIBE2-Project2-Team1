package com.example.campy.controller;

import com.example.campy.dto.material.MaterialListDto;
import com.example.campy.dto.material.request.MaterialRequestDto;
import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.service.CustomUserDetails;
import com.example.campy.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController // REST API 컨트롤러임을 나타내며, 반환 값을 JSON 형식으로 처리
@RequiredArgsConstructor // 생성자 주입 자동 생성
@RequestMapping("/api/materials") // 이 컨트롤러의 기본 URI
public class MaterialController {

    private final MaterialService materialService;

    /**
     * 자료 등록 API
     * @param requestDto - 클라이언트가 보낸 자료 등록 요청 DTO
     * @param userDetails - 인증된 사용자 정보 (JWT 기반)
     */
    @PostMapping
    public ResponseEntity<MaterialResponseDto> createMaterial(
            @RequestBody MaterialRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Integer userId = userDetails.getUserId(); // JWT 인증된 사용자 ID 추출
        MaterialResponseDto response = materialService.createMaterial(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 상태 코드와 함께 응답
    }

    /**
     * 자료 전체 목록 조회 + 페이징 + 정렬 + 키워드 검색 통합 API
     * @param page - 조회할 페이지 번호
     * @param size - 페이지 당 데이터 수
     * @param sort - 정렬 조건 (예: createdAt,desc)
     * @param keyword - 제목 키워드 검색 (nullable)
     */
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

    /**
     * 자료 삭제 API (Soft Delete)
     * @param materialId - 삭제할 자료 ID
     * @param userDetails - 로그인한 사용자 정보 (삭제 권한 확인용)
     */
    @DeleteMapping("/{materialId}")
    public ResponseEntity<String> deleteMaterial(
            @PathVariable Integer materialId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Integer sellerId = userDetails.getUserId(); // 로그인 사용자 ID
        materialService.deleteMaterial(materialId, sellerId);
        return ResponseEntity.ok("자료가 삭제되었습니다.");
    }

    /**
     * 자료 수정 API
     * @param materialId - 수정할 자료 ID
     * @param updateDto - 수정할 내용
     * @param userDetails - 인증 사용자 (권한 확인용)
     */
    @PutMapping("/{materialId}")
    public ResponseEntity<String> updateMaterial(
            @PathVariable Integer materialId,
            @RequestBody MaterialRequestDto updateDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Integer sellerId = userDetails.getUserId();
        materialService.updateMaterial(materialId, sellerId, updateDto);
        return ResponseEntity.ok("자료가 수정되었습니다.");
    }

    /**
     * 자료 상세 조회 API
     * @param materialId - 조회할 자료 ID
     */
    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialResponseDto> getMaterialById(@PathVariable Integer materialId) {
        MaterialResponseDto responseDto = materialService.getMaterialById(materialId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 자료 키워드 검색 API (페이징 포함)
     * @param keyword - 검색할 제목 키워드
     * @param page - 페이지 번호
     * @param size - 페이지 크기
     */
    @GetMapping("/search")
    public ResponseEntity<Page<MaterialListDto>> searchMaterials(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending()); // 기본 정렬 기준: 최신순
        Page<MaterialListDto> materials = materialService.searchMaterials(keyword, pageable);
        return ResponseEntity.ok(materials);
    }

    /**
     * 구매한 자료 파일 다운로드 API
     * 결제 기록이 있는 사용자만 접근 가능
     * @param materialId - 다운로드할 자료 ID
     * @param userDetails - 사용자 인증 정보
     */
    @GetMapping("/{materialId}/download")
    public ResponseEntity<Resource> downloadMaterial(
            @PathVariable Integer materialId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Integer userId = userDetails.getUserId();
        return materialService.downloadMaterialFile(materialId, userId);
    }

    /**
     * 내가 등록한 자료 목록 조회 API (페이징 + 정렬 지원)
     * @param userDetails - 로그인 사용자 정보
     * @param page - 페이지 번호
     * @param size - 페이지 크기
     * @param sort - 정렬 조건 (예: createdAt,asc)
     */
    @GetMapping("/my")
    public ResponseEntity<Page<MaterialListDto>> getMyMaterials(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Integer userId = userDetails.getUserId();

        // 정렬 조건 파싱
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<MaterialListDto> materials = materialService.getMyMaterials(userId, pageable);
        return ResponseEntity.ok(materials);
    }

}
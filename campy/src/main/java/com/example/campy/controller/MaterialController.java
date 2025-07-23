package com.example.campy.controller;

import com.example.campy.dto.material.MaterialListDto;
import com.example.campy.dto.material.request.MaterialRequestDto;
import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;
    //private final JwtUtil jwtUtil;
    //private final UserRepository userRepository;

    //자료 등록
    @PostMapping
    public ResponseEntity<MaterialResponseDto> createMaterial(
            @RequestBody MaterialRequestDto requestDto) {

//         jwt 관련 주석 처리
//        // 1. Bearer 제거
//        String token = authHeader.replace("Bearer ", "");
//
//        // 2. 토큰에서 이메일 꺼냄
//        String email = jwtUtil.getEmail(token);
//
//        // 3. 이메일로 사용자 조회
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
//
//        // 4. 해당 사용자의 id를 sellerId로 사용
//        Integer sellerId = user.getUserId();

        Integer sellerId = 1;

        MaterialResponseDto responseDto = materialService.createMaterial(requestDto, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    //자료 목록 조회
    @GetMapping
    public ResponseEntity<List<MaterialListDto>> getAllMaterials() {
        List<MaterialListDto> materials = materialService.getAllMaterials();
        return ResponseEntity.ok(materials);
    }

    // 자료 삭제
    @DeleteMapping("/{materialId}")
    public ResponseEntity<String> deleteMaterial(@PathVariable Integer materialId) {


        Integer sellerId = 1;

        materialService.deleteMaterial(materialId, sellerId);
        return ResponseEntity.ok("자료가 삭제되었습니다.");
    }


    // 자료 수정
    @PutMapping("/{materialId}")
    public ResponseEntity<String> updateMaterial(
            @PathVariable Integer materialId,
            @RequestBody MaterialRequestDto updateDto) {

        Integer sellerId = 1;

        materialService.updateMaterial(materialId, sellerId, updateDto);
        return ResponseEntity.ok("자료가 수정되었습니다.");
    }


    // 자료 상세 조회
    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialResponseDto> getMaterialById(@PathVariable Integer materialId) {
        MaterialResponseDto responseDto = materialService.getMaterialById(materialId);
        return ResponseEntity.ok(responseDto);
    }

    // 최신순 조회
    @GetMapping("/latest")
    public ResponseEntity<List<MaterialListDto>> getMaterialsLatest() {
        List<MaterialListDto> materials = materialService.getMaterialsOrderByCreatedAtDesc();
        return ResponseEntity.ok(materials);
    }

    // 가격 낮은순
    @GetMapping("/price/asc")
    public ResponseEntity<List<MaterialListDto>> getMaterialsByLowPrice() {
        List<MaterialListDto> materials = materialService.getMaterialsOrderByPriceAsc();
        return ResponseEntity.ok(materials);
    }

    // 가격 높은순
    @GetMapping("/price/desc")
    public ResponseEntity<List<MaterialListDto>> getMaterialsByHighPrice() {
        List<MaterialListDto> materials = materialService.getMaterialsOrderByPriceDesc();
        return ResponseEntity.ok(materials);
    }

    //자료 검색
    @GetMapping("/search")
    public ResponseEntity<List<MaterialListDto>> searchMaterials(@RequestParam("keyword") String keyword) {
        List<MaterialListDto> result = materialService.searchMaterials(keyword);
        return ResponseEntity.ok(result);
    }



}
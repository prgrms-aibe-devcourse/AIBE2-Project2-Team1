package com.example.campy.controller;

import com.example.campy.dto.MaterialListDto;
import com.example.campy.dto.MaterialRequestDto;
import com.example.campy.dto.MaterialResponseDto;
import com.example.campy.entity.User;
import com.example.campy.jwt.JwtUtil;
import com.example.campy.repository.UserRepository;
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
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    //자료 등록
    @PostMapping
    public ResponseEntity<MaterialResponseDto> createMaterial(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MaterialRequestDto requestDto) {

        // 1. Bearer 제거
        String token = authHeader.replace("Bearer ", "");

        // 2. 토큰에서 이메일 꺼냄
        String email = jwtUtil.getEmail(token);

        // 3. 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // 4. 해당 사용자의 id를 sellerId로 사용
        Integer sellerId = user.getUserId();

        MaterialResponseDto responseDto = materialService.createMaterial(requestDto, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    //자료 목록 조회
    @GetMapping
    public ResponseEntity<List<MaterialListDto>> getAllMaterials() {
        List<MaterialListDto> materials = materialService.getAllMaterials();
        return ResponseEntity.ok(materials);
    }


}
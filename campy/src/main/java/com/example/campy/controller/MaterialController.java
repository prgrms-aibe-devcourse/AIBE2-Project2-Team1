package com.example.campy.controller;

import com.example.campy.dto.MaterialRequestDto;
import com.example.campy.dto.MaterialResponseDto;
import com.example.campy.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    public ResponseEntity<MaterialResponseDto> createMaterial(@RequestBody MaterialRequestDto requestDto) {
        Integer sellerId = 1;
        MaterialResponseDto responseDto = materialService.createMaterial(requestDto, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}

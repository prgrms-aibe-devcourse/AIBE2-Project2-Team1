package com.example.campy.service;

import com.example.campy.dto.MaterialRequestDto;
import com.example.campy.dto.MaterialResponseDto;

public interface MaterialService {
    MaterialResponseDto createMaterial(MaterialRequestDto requestDto, Integer sellerId);
}
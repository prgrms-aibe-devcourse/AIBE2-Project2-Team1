package com.example.campy.service;

import com.example.campy.dto.MaterialListDto;
import com.example.campy.dto.MaterialRequestDto;
import com.example.campy.dto.MaterialResponseDto;

import java.util.List;

public interface MaterialService {

    //자료 등록 메서드
    MaterialResponseDto createMaterial(MaterialRequestDto requestDto, Integer sellerId);

    //등록된 전체 자료 목록을 조회하는 메서드
    List<MaterialListDto> getAllMaterials();
}
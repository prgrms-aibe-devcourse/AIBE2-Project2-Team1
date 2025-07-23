package com.example.campy.service;

import com.example.campy.dto.material.MaterialListDto;
import com.example.campy.dto.material.request.MaterialRequestDto;
import com.example.campy.dto.material.response.MaterialResponseDto;

import java.util.List;

public interface MaterialService {

    //자료 등록 메서드
    MaterialResponseDto createMaterial(MaterialRequestDto requestDto, Integer sellerId);

    //등록된 전체 자료 목록을 조회하는 메서드
    List<MaterialListDto> getAllMaterials();

    //자료 삭제 메서드
    void deleteMaterial(Integer materialId, Integer sellerId);

    //자료 수정 메서드
    void updateMaterial(Integer materialId, Integer sellerId, MaterialRequestDto updateDto);

    //자료 상세 조회
    MaterialResponseDto getMaterialById(Integer materialId);

    List<MaterialListDto> getMaterialsOrderByCreatedAtDesc();
    List<MaterialListDto> getMaterialsOrderByPriceAsc();
    List<MaterialListDto> getMaterialsOrderByPriceDesc();

    List<MaterialListDto> searchMaterials(String keyword);
}
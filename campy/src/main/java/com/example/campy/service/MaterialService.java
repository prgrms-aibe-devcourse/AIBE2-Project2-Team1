package com.example.campy.service;

import com.example.campy.dto.material.MaterialListDto;
import com.example.campy.dto.material.request.MaterialRequestDto;
import com.example.campy.dto.material.response.MaterialResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface MaterialService {
    MaterialResponseDto createMaterial(MaterialRequestDto requestDto, String username);
    Page<MaterialListDto> getAllMaterials(int page, int size, String sortParam, String keyword);
    void deleteMaterial(Integer materialId, String username);
    MaterialResponseDto updateMaterial(Integer materialId, String username, MaterialRequestDto requestDto);
    MaterialResponseDto getMaterialById(Integer materialId);
    Page<MaterialListDto> searchMaterials(String keyword, Pageable pageable);
    ResponseEntity<Resource> downloadMaterialFile(Integer materialId, String username);
    Page<MaterialListDto> getMyMaterials(String username, Pageable pageable);
}
package com.example.campy.service;

import com.example.campy.dto.material.request.MaterialCreateRequest;
import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.dto.material.request.MaterialUpdateRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MaterialService {
    MaterialResponseDto createMaterial(MaterialCreateRequest request, MultipartFile materialFile, MultipartFile thumbnail, Authentication authentication) throws IOException;
    List<MaterialResponseDto> getAllMaterials();
    List<MaterialResponseDto> getAllMaterialsByUser(Authentication authentication);
    MaterialResponseDto getMaterialById(Integer materialId);
    void updateMaterial(Integer materialId, MaterialUpdateRequest request, Authentication authentication);
    void deleteMaterial(Integer materialId, Authentication authentication);
}

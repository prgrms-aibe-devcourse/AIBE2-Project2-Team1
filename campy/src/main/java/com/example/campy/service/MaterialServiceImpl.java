package com.example.campy.service;

import com.example.campy.dto.material.MaterialListDto;
import com.example.campy.dto.material.request.MaterialRequestDto;
import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.entity.Material;
import com.example.campy.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    public MaterialResponseDto createMaterial(MaterialRequestDto requestDto, Integer sellerId) {
        Material material = Material.builder()
                .sellerId(sellerId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .fileUrl(requestDto.getFileUrl())
                .previewFileUrl(requestDto.getPreviewFileUrl())
                .thumbnailUrl(requestDto.getThumbnailUrl())
                .price(requestDto.getPrice())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Material saved = materialRepository.save(material);

        return new MaterialResponseDto(saved);
    }

    @Override
    public List<MaterialListDto> getAllMaterials() {
        List<Material> materials = materialRepository.findAll();

        return materials.stream()
                .map(material -> MaterialListDto.builder()
                        .materialId(material.getMaterialId())
                        .title(material.getTitle())
                        .thumbnailUrl(material.getThumbnailUrl())
                        .price(material.getPrice())
                        .createdAt(material.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMaterial(Integer materialId, Integer sellerId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));

        // sellerId가 일치하는지 확인 (본인 자료만 삭제 가능)
        if (!material.getSellerId().equals(sellerId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 실제 삭제가 아니라 isDeleted = true로 soft delete 처리
        material.setIsDeleted(true);
        material.setUpdatedAt(LocalDateTime.now());

        materialRepository.save(material);
    }

    @Override
    public void updateMaterial(Integer materialId, Integer sellerId, MaterialRequestDto requestDto) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));

        if (!material.getSellerId().equals(sellerId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 수정할 필드만 갱신
        material.setTitle(requestDto.getTitle());
        material.setContent(requestDto.getContent());
        material.setFileUrl(requestDto.getFileUrl());
        material.setPreviewFileUrl(requestDto.getPreviewFileUrl());
        material.setThumbnailUrl(requestDto.getThumbnailUrl());
        material.setPrice(requestDto.getPrice());
        material.setUpdatedAt(LocalDateTime.now());

        materialRepository.save(material);
    }

    @Override
    public MaterialResponseDto getMaterialById(Integer materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));

        return new MaterialResponseDto(material);
    }

    //  최신순 정렬
    @Override
    public List<MaterialListDto> getMaterialsOrderByCreatedAtDesc() {
        List<Material> materials = materialRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
        return materials.stream()
                .map(MaterialListDto::new)
                .toList();
    }

    //  가격 오름차순
    @Override
    public List<MaterialListDto> getMaterialsOrderByPriceAsc() {
        List<Material> materials = materialRepository.findByIsDeletedFalseOrderByPriceAsc();
        return materials.stream()
                .map(MaterialListDto::new)
                .toList();
    }

    //  가격 내림차순
    @Override
    public List<MaterialListDto> getMaterialsOrderByPriceDesc() {
        List<Material> materials = materialRepository.findByIsDeletedFalseOrderByPriceDesc();
        return materials.stream()
                .map(MaterialListDto::new)
                .toList();
    }

    @Override
    public List<MaterialListDto> searchMaterials(String keyword) {
        List<Material> materials = materialRepository.findByTitleContainingIgnoreCase(keyword);
        return materials.stream()
                .map(MaterialListDto::new)
                .collect(Collectors.toList());
    }


}
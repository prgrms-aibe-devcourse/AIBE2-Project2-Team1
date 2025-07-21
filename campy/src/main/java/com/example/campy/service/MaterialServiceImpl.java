package com.example.campy.service;

import com.example.campy.dto.MaterialRequestDto;
import com.example.campy.dto.MaterialResponseDto;
import com.example.campy.entity.Material;
import com.example.campy.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        return new MaterialResponseDto(
                saved.getMaterialId(),
                saved.getSellerId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getFileUrl(),
                saved.getPreviewFileUrl(),
                saved.getThumbnailUrl(),
                saved.getPrice(),
                saved.getCreatedAt()
        );
    }
}
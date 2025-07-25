package com.example.campy.service;

import com.example.campy.dto.material.MaterialListDto;
import com.example.campy.dto.material.request.MaterialRequestDto;
import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.entity.Material;
import com.example.campy.entity.User;
import com.example.campy.repository.MaterialRepository;
import com.example.campy.repository.PaymentRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public MaterialResponseDto createMaterial(MaterialRequestDto requestDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        Material material = Material.builder()
                .seller(user)
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
    public Page<MaterialListDto> getAllMaterials(int page, int size, String sortParam, String keyword) {
        String[] sortParts = sortParam.split(",");
        String sortBy = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Material> materialPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            materialPage = materialRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable);
        } else {
            materialPage = materialRepository.findByIsDeletedFalse(pageable);
        }

        return materialPage.map(MaterialListDto::new);
    }

    @Override
    public void deleteMaterial(Integer materialId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자입니다."));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));

        if (!material.getSeller().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        material.setIsDeleted(true);
        material.setUpdatedAt(LocalDateTime.now());
        materialRepository.save(material);
    }

    @Override
    public MaterialResponseDto updateMaterial(Integer materialId, String username, MaterialRequestDto requestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자입니다."));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));

        if (!material.getSeller().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        material.setTitle(requestDto.getTitle());
        material.setContent(requestDto.getContent());
        material.setFileUrl(requestDto.getFileUrl());
        material.setPreviewFileUrl(requestDto.getPreviewFileUrl());
        material.setThumbnailUrl(requestDto.getThumbnailUrl());
        material.setPrice(requestDto.getPrice());
        material.setUpdatedAt(LocalDateTime.now());

        Material updated = materialRepository.save(material);
        return new MaterialResponseDto(updated);
    }

    @Override
    public MaterialResponseDto getMaterialById(Integer materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));
        return new MaterialResponseDto(material);
    }

    @Override
    public Page<MaterialListDto> searchMaterials(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return materialRepository.findByIsDeletedFalse(pageable)
                    .map(MaterialListDto::new);
        }

        return materialRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable)
                .map(MaterialListDto::new);
    }

    @Override
    public ResponseEntity<Resource> downloadMaterialFile(Integer materialId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자입니다."));

        boolean isBuyer = paymentRepository.existsByBuyer_UserIdAndTargetIdAndType(
                user.getUserId(), materialId, "자료"
        );
        if (!isBuyer) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Material material = materialRepository.findByMaterialIdAndIsDeletedFalse(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));

        String filePath = material.getFileUrl().replace("https://example.com", "C:/campyTest");
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드 중 오류 발생", e);
        }
    }

    @Override
    public Page<MaterialListDto> getMyMaterials(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자입니다."));

        Page<Material> page = materialRepository.findBySeller_UserIdAndIsDeletedFalse(user.getUserId(), pageable);
        return page.map(MaterialListDto::new);
    }
}
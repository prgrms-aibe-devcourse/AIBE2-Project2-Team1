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

    /**
     * 자료 등록 메서드
     * @param requestDto 자료 등록을 위한 입력값 (제목, 내용, 썸네일 등)
     * @param userId 등록자(판매자)의 userId (토큰에서 가져옴)
     * @return 생성된 자료의 상세 DTO
     */
    @Override
    public MaterialResponseDto createMaterial(MaterialRequestDto requestDto, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 자료 객체 생성 및 저장
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


    // 전체 자료 목록 조회 (페이징 + 정렬 + 키워드 검색 포함)
    @Override
    public Page<MaterialListDto> getAllMaterials(int page, int size, String sortParam, String keyword) {
        // sort 파라미터 분리 (예: createdAt,desc)
        String[] sortParts = sortParam.split(",");
        String sortBy = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Material> materialPage;

        // 키워드가 있으면 제목 검색, 없으면 전체
        if (keyword != null && !keyword.trim().isEmpty()) {
            materialPage = materialRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable);
        } else {
            materialPage = materialRepository.findByIsDeletedFalse(pageable);
        }

        return materialPage.map(MaterialListDto::new);
    }

    // 자료 삭제 (Soft Delete 처리)
    @Override
    public void deleteMaterial(Integer materialId, Integer sellerId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));

        // 작성자(판매자)만 삭제 가능
        if (!material.getSeller().getUserId().equals(sellerId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // Soft Delete 처리
        material.setIsDeleted(true);
        material.setUpdatedAt(LocalDateTime.now());
        materialRepository.save(material);
    }

    //자료 수정
    @Override
    public MaterialResponseDto updateMaterial(Integer materialId, Integer sellerId, MaterialRequestDto requestDto) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));

        // 작성자(판매자)만 수정 가능
        if (!material.getSeller().getUserId().equals(sellerId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 수정 항목 반영
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

    //자료 상세 조회
    @Override
    public MaterialResponseDto getMaterialById(Integer materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));
        return new MaterialResponseDto(material);
    }

    // 키워드로 자료 검색 (페이징 포함)
    @Override
    public Page<MaterialListDto> searchMaterials(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return materialRepository.findByIsDeletedFalse(pageable)
                    .map(MaterialListDto::new);
        }

        return materialRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(keyword, pageable)
                .map(MaterialListDto::new);
    }

    //자료 다운로드 기능 (구매자만 가능)
    @Override
    public ResponseEntity<Resource> downloadMaterialFile(Integer materialId, Integer userId) {
        // 결제 여부 확인
        boolean isBuyer = paymentRepository.existsByBuyer_UserIdAndTargetIdAndType(
                userId, materialId, "자료"
        );
        if (!isBuyer) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 자료 존재 여부 확인
        Material material = materialRepository.findByMaterialIdAndIsDeletedFalse(materialId)
                .orElseThrow(() -> new RuntimeException("자료를 찾을 수 없습니다."));

        // 실제 파일 경로 변환 (가상의 URL → 실제 로컬 저장소)
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

     // 내가 등록한 자료 목록 조회 (Soft Delete 제외)
    @Override
    public Page<MaterialListDto> getMyMaterials(Integer userId, Pageable pageable) {
        Page<Material> page = materialRepository.findBySeller_UserIdAndIsDeletedFalse(userId, pageable);
        return page.map(MaterialListDto::new);
    }

}
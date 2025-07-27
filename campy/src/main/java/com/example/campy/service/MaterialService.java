package com.example.campy.service;


import com.example.campy.dto.material.request.MaterialCreateRequest;
import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.dto.material.request.MaterialUpdateRequest;
import com.example.campy.entity.User;
import com.example.campy.repository.MaterialRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.example.campy.entity.Material;
import com.example.campy.exception.GeneralException;
import com.example.campy.constant.ErrorCode;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    private File uploadDir; // 클래스 멤버 변수로 선언

    @PostConstruct
    public void initUploadDir() {
        uploadDir = new File(uploadPath); // 초기화
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Transactional
    public MaterialResponseDto createMaterial(MaterialCreateRequest request, MultipartFile materialFile, MultipartFile thumbnail, Authentication authentication) throws IOException {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다: " + username));

        String fileUrl = null;
        if (materialFile != null && !materialFile.isEmpty()) {
            String materialFileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(materialFile.getOriginalFilename());
            File dest = new File(uploadDir, materialFileName);
            materialFile.transferTo(dest);
            fileUrl = materialFileName;
        }

        String thumbnailUrl = null;
        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailFileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(thumbnail.getOriginalFilename());
            File dest = new File(uploadDir, thumbnailFileName);
            thumbnail.transferTo(dest);
            thumbnailUrl = thumbnailFileName;
        }

        Material material = request.toEntity(currentUser, fileUrl, thumbnailUrl);
        Material savedMaterial = materialRepository.save(material);

        return MaterialResponseDto.from(savedMaterial);
    }

    public List<MaterialResponseDto> getAllMaterials() {
        return materialRepository.findByIsDeletedFalse().stream()
                .map(MaterialResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<MaterialResponseDto> getAllMaterialsByUser(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return materialRepository.findBySellerAndIsDeletedFalse(currentUser).stream()
                .map(MaterialResponseDto::from)
                .collect(Collectors.toList());
    }

    public MaterialResponseDto getMaterialById(Integer materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "자료를 찾을 수 없습니다."));
        return MaterialResponseDto.from(material);
    }

    @Transactional
    public void updateMaterial(Integer materialId, MaterialUpdateRequest request, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다: " + username));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "자료를 찾을 수 없습니다."));

        if (!material.getSeller().getUserId().equals(currentUser.getUserId())) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED, "자료를 수정할 권한이 없습니다.");
        }

        material.setTitle(request.title());
        material.setContent(request.content());
        material.setPrice(request.price());
        material.setIsDeleted(request.isDeleted());
        material.setUpdatedAt(LocalDateTime.now());

        materialRepository.save(material);
    }

    @Transactional
    public void deleteMaterial(Integer materialId, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다: " + username));

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "자료를 찾을 수 없습니다."));

        if (!material.getSeller().getUserId().equals(currentUser.getUserId())) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED, "자료를 삭제할 권한이 없습니다.");
        }

        material.setIsDeleted(true);
        material.setUpdatedAt(LocalDateTime.now());
        materialRepository.save(material);
    }

}
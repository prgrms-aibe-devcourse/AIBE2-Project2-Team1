package com.example.campy.service;

import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.dto.material.request.MaterialUpdateRequest;
import com.example.campy.entity.User;
import com.example.campy.repository.MaterialRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;
import com.example.campy.entity.Material;
import com.example.campy.exception.GeneralException;
import com.example.campy.constant.ErrorCode;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

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
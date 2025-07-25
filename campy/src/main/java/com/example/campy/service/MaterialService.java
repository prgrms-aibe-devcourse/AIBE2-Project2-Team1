package com.example.campy.service;

import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.entity.User;
import com.example.campy.repository.MaterialRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
}
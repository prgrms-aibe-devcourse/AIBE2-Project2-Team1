package com.example.campy.controller;


import com.example.campy.dto.material.request.MaterialCreateRequest;

import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.util.*;

@Controller

@RequiredArgsConstructor
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;


    @Value("${upload.path}")
    private String uploadPath;

    // 더미 데이터 초기화 블록 제거

    // 📄 자료 리스트
    @GetMapping("/materials")
    public String showMaterialList(Model model) {
        model.addAttribute("materials", materialService.getAllMaterials());
        return "materials/materialList";


    }

    //자료 삭제
    @DeleteMapping("/{materialId}")
    public ResponseEntity<String> deleteMaterial(
            @PathVariable Integer materialId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        materialService.deleteMaterial(materialId, username);
        return ResponseEntity.ok("자료가 삭제되었습니다.");
    }


    @PostMapping(value = "/materials/new", consumes = {"multipart/form-data"}, produces = "application/json")
    public ResponseEntity<MaterialResponseDto> submitMaterialForm(
            @RequestPart("data") MaterialCreateRequest request,
            @RequestPart(value = "materialFile", required = false) MultipartFile materialFile,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            Authentication authentication) {
        try {
            MaterialResponseDto newMaterial = materialService.createMaterial(request, materialFile, thumbnail, authentication);

            return ResponseEntity.ok(newMaterial);

        } catch (IOException e) {
            e.printStackTrace();
            // 오류 발생 시 적절한 HTTP 상태 코드와 메시지를 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 🧐 상세 보기
    @GetMapping("/materials/{id}")
    public String showMaterialDetail(@PathVariable Integer id, Model model) {
        MaterialResponseDto material = materialService.getMaterialById(id);
        if (material == null) return "error/404"; // 서비스에서 예외 처리하므로 null 반환될 일은 없지만, 방어적 코드

        model.addAttribute("material", material);
        return "materials/materialDetail";
    }

    // 💳 구매 페이지
    @GetMapping("/materials/{id}/purchase")
    public String showPurchasePage(@PathVariable Integer id, Model model) {
        MaterialResponseDto material = materialService.getMaterialById(id);
        if (material == null) return "error/404";

        model.addAttribute("material", material);
        return "materials/materialPurchase";
    }

    // ✅ 결제 완료 후 다운로드 제공
    @PostMapping("/materials/payment")
    public String completePurchase(@RequestParam("materialId") Integer materialId, Model model) {
        MaterialResponseDto material = materialService.getMaterialById(materialId);

        if (material == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "자료가 존재하지 않습니다.");
        }

        // MaterialResponseDto에서 fileUrl을 직접 가져옵니다.
        String downloadLink = "/uploads/" + material.fileUrl();

        model.addAttribute("material", material);
        model.addAttribute("message", "결제가 완료되었습니다. 자료 다운로드가 가능합니다.");
        model.addAttribute("downloadLink", downloadLink);


    //내가 등록한 자료 목록 조회
    @GetMapping("/my")
    public ResponseEntity<Page<MaterialListDto>> getMyMaterials(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String username = authentication.getName();

        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<MaterialListDto> materials = materialService.getMyMaterials(username, pageable);
        return ResponseEntity.ok(materials);
    }
}
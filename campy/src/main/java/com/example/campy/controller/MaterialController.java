package com.example.campy.controller;

import com.example.campy.dto.MaterialDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MaterialController {

    private final Map<Long, MaterialDto> dummyMaterials = new HashMap<>();
    private final Map<Long, String> materialFilePaths = new HashMap<>();

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    {
        dummyMaterials.put(1L, new MaterialDto(
                1L,
                "00대학교 000교수님 JAVA강의의 25-1 족보",
                "000",
                "5000",
                "2025-07-17 14:33:05",
                "/images/thumb_1.jpg",
                "프로그래밍",
                "JAVA 시험 A+ 받은 족보입니다."
        ));
        materialFilePaths.put(1L, "/uploads/sample1.pdf");

        dummyMaterials.put(2L, new MaterialDto(
                2L,
                "00대학교 000교수님 컴퓨터 구조 25-1 족보",
                "ooo",
                "3000",
                "2025-07-17 14:33:05",
                "/images/thumb_2.jpg",
                "컴퓨터구조",
                "컴퓨터 구조 핵심 요약 정리된 족보입니다."
        ));
        materialFilePaths.put(2L, "/uploads/sample2.pdf");

        dummyMaterials.put(3L, new MaterialDto(
                3L,
                "우분투 리눅스 요약본",
                "ㅇㅇㅇ",
                "4000",
                "2025-07-17 14:33:05",
                "/images/thumb_3.jpg",
                "운영체제",
                "우분투 사용법을 깔끔하게 정리한 요약본입니다."
        ));
        materialFilePaths.put(3L, "/uploads/sample3.pdf");
    }

    // 📄 자료 리스트
    @GetMapping("/materials")
    public String showMaterialList(Model model) {
        model.addAttribute("materials", dummyMaterials.values());
        return "materials/materialList";
    }

    // 📝 자료 등록 페이지
    @GetMapping("/materials/new")
    public String showNewMaterialForm() {
        return "materials/newMaterial";
    }

    @PostMapping("/materials/new")
    public String submitMaterialForm(@RequestParam("materialFile") MultipartFile materialFile,
                                     @RequestParam("thumbnail") MultipartFile thumbnail,
                                     @RequestParam("title") String title,
                                     @RequestParam("category") String category,
                                     @RequestParam("description") String description,
                                     @RequestParam("price") int price,
                                     Model model) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + materialFile.getOriginalFilename();
            String thumbnailName = UUID.randomUUID() + "_" + thumbnail.getOriginalFilename();

            materialFile.transferTo(new File(UPLOAD_DIR + fileName));
            thumbnail.transferTo(new File(UPLOAD_DIR + thumbnailName));

            Long newId = (long) (dummyMaterials.size() + 1);
            String now = LocalDateTime.now().toString();

            MaterialDto newMaterial = new MaterialDto(
                    newId,
                    title,
                    "사용자",  // 로그인 사용자 정보 대체 가능
                    String.valueOf(price),
                    now,
                    "/uploads/" + thumbnailName,
                    category,
                    description
            );

            dummyMaterials.put(newId, newMaterial);
            materialFilePaths.put(newId, "/uploads/" + fileName);

            model.addAttribute("material", newMaterial);
            model.addAttribute("downloadLink", "/uploads/" + fileName);
            model.addAttribute("message", "자료가 성공적으로 등록되었습니다.");

            return "materials/purchaseSuccessPage";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "파일 업로드 실패");
            return "error/500";
        }
    }

    // 🧐 상세 보기
    @GetMapping("/materials/{id}")
    public String showMaterialDetail(@PathVariable Long id, Model model) {
        MaterialDto material = dummyMaterials.get(id);
        if (material == null) return "error/404";

        model.addAttribute("material", material);
        return "materials/materialDetail";
    }

    // 💳 구매 페이지
    @GetMapping("/materials/{id}/purchase")
    public String showPurchasePage(@PathVariable Long id, Model model) {
        MaterialDto material = dummyMaterials.get(id);
        if (material == null) return "error/404";

        model.addAttribute("material", material);
        return "materials/materialPurchase";
    }

    // ✅ 결제 완료 후 다운로드 제공
    @PostMapping("/materials/payment")
    public String completePurchase(@ModelAttribute MaterialDto material, Model model) {
        Long id = material.getId();
        MaterialDto target = dummyMaterials.get(id);

        if (material == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "자료가 존재하지 않습니다.");
        }

        String downloadLink = materialFilePaths.get(id);

        model.addAttribute("material", target);
        model.addAttribute("message", "결제가 완료되었습니다. 자료 다운로드가 가능합니다.");
        model.addAttribute("downloadLink", downloadLink);

        return "materials/purchaseSuccessPage";
    }
}

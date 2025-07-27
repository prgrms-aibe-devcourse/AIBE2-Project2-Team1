package com.example.campy.controller;

import com.example.campy.dto.talent.response.TalentResponseDto;
import com.example.campy.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final TalentService talentService;

    // 결제 폼
    @GetMapping("/new")
    public String paymentForm(@RequestParam("talentId") Integer talentId, Model model, Authentication authentication) {
        TalentResponseDto talent = talentService.getTalentById(talentId);
        model.addAttribute("talent", talent);
        // (구매자 정보 필요하면 authentication에서 username 등 꺼내서 넘기면 됨)
        return "payments/payment-form";
    }

    // 결제 처리 (실제 결제 로직 없이 완료 페이지로 이동)
    @PostMapping("/pay")
    public String paymentComplete(@RequestParam("talentId") Integer talentId) {
        // 실제 결제 처리 로직 생략
        return "redirect:/payments/complete";
    }

    // 결제 완료 페이지 (팝업 후 메인으로)
    @GetMapping("/complete")
    public String paymentSuccess() {
        return "payments/complete";
    }
}
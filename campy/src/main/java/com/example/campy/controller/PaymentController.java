package com.example.campy.controller;

import com.example.campy.entity.Payment;
import com.example.campy.service.CustomUserDetails;
import com.example.campy.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/mypage/activity/purchases")
    public String userPaymentsPage(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUserId();
        List<Payment> payments = paymentService.getUserPayments(userId);
        model.addAttribute("payments", payments);
        return "mypage/purchases";
    }

    
}

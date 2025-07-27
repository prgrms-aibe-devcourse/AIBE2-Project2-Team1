package com.example.campy.service;

import com.example.campy.entity.Payment;
import com.example.campy.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<Payment> getUserPayments(Integer userId) {
        return paymentRepository.findByBuyerUserId(userId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}

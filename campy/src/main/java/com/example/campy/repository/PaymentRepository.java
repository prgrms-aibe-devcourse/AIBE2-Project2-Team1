package com.example.campy.repository;

import com.example.campy.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByBuyerUserId(Integer userId);
}

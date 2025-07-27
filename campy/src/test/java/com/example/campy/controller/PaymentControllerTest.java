package com.example.campy.controller;

import com.example.campy.entity.Payment;
import com.example.campy.entity.User;
import com.example.campy.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getUserPayments() throws Exception {
        User user = new User();
        user.setUserId(1L);

        Payment payment = new Payment();
        payment.setPaymentId(1);
        payment.setBuyer(user);

        when(paymentService.getUserPayments(1L)).thenReturn(Collections.singletonList(payment));

        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllPayments() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/payments"))
                .andExpect(status().isOk());
    }
}

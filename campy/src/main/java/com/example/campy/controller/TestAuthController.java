package com.example.campy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAuthController {

    @GetMapping("/user/test")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userAccess() {
        return "✅ USER 권한 접근 성공";
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "✅ ADMIN 권한 접근 성공";
    }
}

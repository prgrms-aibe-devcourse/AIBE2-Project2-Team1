package com.example.campy.controller;

import com.example.campy.dto.LoginForm;
import com.example.campy.dto.SignUpForm;
import com.example.campy.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginForm form) {
        String token = authService.login(form);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpForm form) {
        authService.signUp(form);
        return ResponseEntity.ok().build();
    }
}

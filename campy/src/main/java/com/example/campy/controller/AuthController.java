package com.example.campy.controller;

import com.example.campy.dto.LoginForm;
import com.example.campy.dto.SignUpForm;
import com.example.campy.service.AuthService;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginForm form, HttpServletResponse response) {
        String token = authService.login(form);

        // Create a cookie
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true); // Make it HttpOnly for security
        cookie.setPath("/"); // Set path to root so it's sent with all requests
        cookie.setMaxAge(7 * 24 * 60 * 60); // Set cookie expiry (e.g., 7 days) - adjust as needed

        response.addCookie(cookie); // Add the cookie to the response

        return ResponseEntity.ok().body("Login successful"); // Return a simple message, token is in cookie
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpForm form) {
        authService.signUp(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);  // 이름은 기존과 같게
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);  // 즉시 만료
        response.addCookie(cookie);

        return ResponseEntity.ok("Logout successful");
    }
}

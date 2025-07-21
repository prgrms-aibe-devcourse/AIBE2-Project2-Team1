package com.example.campy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/") // 기본 경로 설정
public class PageController {

    @GetMapping
    public String mainPage() {
        return "main"; // → templates/main.html 렌더링
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login/login"; // → templates/login/login.html 렌더링
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup/signup"; // → templates/signup/signup.html 렌더링
    }
}
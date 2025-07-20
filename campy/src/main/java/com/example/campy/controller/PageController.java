package com.example.campy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // → templates/login.html 렌더링
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // → templates/signup.html 렌더링
    }
}
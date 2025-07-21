package com.example.campy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String mainPage() {
        return "main.html";
    }

    @GetMapping("auth/login")
    public String loginPage() {
        return "login"; // → templates/login.html 렌더링
    }

}
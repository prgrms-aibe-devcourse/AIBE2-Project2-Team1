package com.example.campy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ai")
public class AiController {

    @GetMapping("/start")
    public String startPage() {
        return "ai/start";
    }

    @GetMapping("/loading")
    public String loadingPage() {
        return "ai/loading";
    }

    @GetMapping("/result")
    public String resultPage() {
        return "ai/result";
    }
}


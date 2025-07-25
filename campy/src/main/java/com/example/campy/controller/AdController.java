package com.example.campy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdController {

    @GetMapping("/ai/start")
    public String aiStartPage() {
        return "ai/start"; // templates/ai/start.html
    }

    @GetMapping("/telents/telentList")
    public String telentListPage() {
        return "telents/telentList"; // templates/telents/telentList.html
    }

    @GetMapping("/meterials/materialList")
    public String materialListPage() {
        return "meterials/materialList"; // templates/meterials/materialList.html
    }
}

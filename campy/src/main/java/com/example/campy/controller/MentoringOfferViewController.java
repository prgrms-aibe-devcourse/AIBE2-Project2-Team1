package com.example.campy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MentoringOfferViewController {

    @GetMapping("/mentors")
    public String mentorsPage() {
        return "classes/classList"; // mentors.html 리턴
    }

}

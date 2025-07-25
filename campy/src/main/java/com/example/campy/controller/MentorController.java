package com.example.campy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MentorController {

    @GetMapping("/mentor/detail")
    public String showMentorDetailPage() {
        // 더미 데이터 기반 정적 페이지 보여주기 (model 불필요)
        return "classes/mentorDetail"; // templates/mentorDetail.html 렌더링
    }
}

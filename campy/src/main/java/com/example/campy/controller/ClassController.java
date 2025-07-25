package com.example.campy.controller;

import com.example.campy.dto.ClassDto;
import com.example.campy.entity.ClassDetail;
import com.example.campy.entity.Mentor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClassController {

    @GetMapping("/newClass")
    public String showNewClassForm() {
        return "classes/newClass";
    }

    @GetMapping("/newClassComplete")
    public String showCompletePage() {
        return "classes/newClassComplete";
    }

    @GetMapping("/classList")
    public String classList(@RequestParam(required = false) String query,
                            @RequestParam(required = false) String tag,
                            Model model) {

        List<ClassDto> classes = new ArrayList<>();

        // 더미 데이터
        ClassDto c1 = new ClassDto("컴퓨터 공학과 취업 상담", "컴공 진로 및 취업 관련 상담", "권은비", "/images/eunbi.jpg", List.of("컴공", "포폴", "진로"));
        ClassDto c2 = new ClassDto("코딩 테스트 준비", "다양한 유형의 문제 풀이", "차은우", "/images/eunwoo.jpg", List.of("코딩", "테스트"));
        classes.add(c1);
        classes.add(c2);

        model.addAttribute("classes", classes);
        model.addAttribute("tagList", List.of("컴공", "포폴", "진로", "테스트"));

        return "classes/classList";
    }

    @GetMapping("/class/detail/{id}")
    public String showClassDetail(@PathVariable("id") Long classId, Model model) {
        // 🧪 더미 데이터 생성
        ClassDetail classDetail = new ClassDetail(
                classId,
                "spring의 기본",
                "멘토/코딩/spring",
                50000,
                "Discord, Offline",
                1L // mentorId
        );

        Mentor mentor = new Mentor(
                1L,
                "김영한",
                "자바/스프링 프레임워크 전문가",
                "/images/mentor_profile.jpg"
        );

        // 💡 모델에 데이터 담기
        model.addAttribute("classDetail", classDetail);
        model.addAttribute("mentor", mentor);

        return "classes/classDetail"; // 이 HTML을 렌더링
    }

    @GetMapping("/class/classPurchase")
    public String showMentorDetail() {
        return "classes/classPurchase";
    }

    // 수업 구매 페이지
    @GetMapping("/class/purchase")
    public String showPurchasePage() {
        return "classes/classPurchase";
    }
}

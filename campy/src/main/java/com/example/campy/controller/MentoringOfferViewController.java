package com.example.campy.controller;

import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.service.MentoringOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class MentoringOfferViewController {

    private final MentoringOfferService mentoringOfferService;

    @GetMapping("/mentors")
    public String redirectMentorList() {
        return "redirect:/classList";
    }

    @GetMapping("/classList")
    public String classList(Model model, Pageable pageable) {
        Page<MentoringOfferResponse> page = mentoringOfferService.findAll(pageable);
        model.addAttribute("offerPage", page);
        return "classes/classList";
    }

    @GetMapping("/class/detail/{offerId}")
    public String classDetail(@PathVariable Integer offerId, Model model) {
        MentoringOfferResponse offer = mentoringOfferService.findById(offerId);
        model.addAttribute("offer", offer);
        return "classes/classDetail";
    }

    @GetMapping("/class/create")
    public String createClass(){
        return "classes/newClass.html";
    }

    @PostMapping("/class/create")
    public String createClass(@ModelAttribute MentoringOfferCreateRequest req){

        Integer userId = 1234; // userId를 받아와야함

        return "classes/newClass.html";
    }

}

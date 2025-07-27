package com.example.campy.controller;

import com.example.campy.dto.mentoring.request.MentoringMatchCreateCombinedRequest;
import com.example.campy.dto.mentoring.request.MentoringMatchCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchResponse;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.service.CustomUserDetails;
import com.example.campy.service.MentoringMatchService;
import com.example.campy.service.MentoringOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class MentoringOfferViewController {

    private final MentoringOfferService mentoringOfferService;
    private final MentoringMatchService matchService;

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
        return "classes/newClass";
    }

    @PostMapping("/class/create")
    public String createClass(@ModelAttribute MentoringOfferCreateRequest req){

        /* userid 받아오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        */

        Integer userId = 1;
        MentoringOfferResponse res = mentoringOfferService.create(req, userId);

        return "classes/newClassComplete";
    }

    @GetMapping("/class/purchase/{offerId}")
    public String handlePurchase(@PathVariable Integer offerId, Model model) {

        MentoringOfferResponse offer = mentoringOfferService.findById(offerId);
        model.addAttribute("offer", offer);

        return "classes/classPurchase";
    }

    @PostMapping("/class/purchase")
    public String handlePurchase(@ModelAttribute MentoringMatchCreateCombinedRequest req, Model model) {
        MentoringMatchResponse res = matchService.createMemberMatch(req);
        model.addAttribute("match", res);
        return "classes/complete";
    }


}
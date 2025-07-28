package com.example.campy.controller;

import com.example.campy.dto.talent.request.TalentUpdateRequest;
import com.example.campy.dto.talent.response.TalentResponseDto;
import com.example.campy.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/talents")
public class TalentViewController {

    private final TalentService talentService;

    

    @GetMapping("/register-success")
    public String showRegisterSuccess() {
        return "talents/register-success";
    }

    @GetMapping("/detail/{id}")
    public String showTalentDetail(@PathVariable Integer id, Model model) {
        TalentResponseDto talent = talentService.getTalentById(id);
        model.addAttribute("talent", talent);
        return "talents/detail";
    }

    @GetMapping("/list")
    public String showTalentList() {
        return "talents/list";
    }
}

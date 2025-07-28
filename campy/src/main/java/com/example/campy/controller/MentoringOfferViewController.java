package com.example.campy.controller;

import com.example.campy.constant.MatchRole;
import com.example.campy.dto.mentoring.request.MentoringMatchCreateCombinedRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchMemberResponse;
import com.example.campy.dto.mentoring.response.MentoringMatchResponse;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.service.MentoringMatchMemberService;
import com.example.campy.service.MentoringMatchService;
import com.example.campy.service.MentoringOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/class")
@RequiredArgsConstructor
public class MentoringOfferViewController {

    private final MentoringOfferService mentoringOfferService;
    private final MentoringMatchService matchService;
    private final MentoringMatchMemberService matchMemberService;


    @GetMapping("/classList")
    public String classList(Model model, Pageable pageable) {
        Page<MentoringOfferResponse> page = mentoringOfferService.findAll(pageable);
        model.addAttribute("offerPage", page);
        return "classes/classList";
    }

    @GetMapping("/detail/{offerId}")
    public String classDetail(@PathVariable Integer offerId, Model model) {
        MentoringOfferResponse offer = mentoringOfferService.findById(offerId);
        model.addAttribute("offer", offer);
        return "classes/classDetail";
    }

    @GetMapping("/create")
    public String createClass(){
        return "classes/newClass";
    }

    @PostMapping("/create")
    public String createClass(@ModelAttribute MentoringOfferCreateRequest req){

        /* userid 받아오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        */

        Integer userId = 1;
        MentoringOfferResponse res = mentoringOfferService.create(req, userId);

        return "classes/newClassComplete";
    }

    @GetMapping("/purchase/{offerId}")
    public String handlePurchase(@PathVariable Integer offerId, Model model) {

        MentoringOfferResponse offer = mentoringOfferService.findById(offerId);
        model.addAttribute("offer", offer);

        return "classes/classPurchase";
    }

    @PostMapping("/purchase")
    public String handlePurchase(@ModelAttribute MentoringMatchCreateCombinedRequest req, Model model) {
        MentoringMatchResponse res = matchService.createMemberMatch(req);
        model.addAttribute("match", res);
        return "classes/complete";
    }

    @GetMapping("/search")
    public String searchMentoringClass(
            @RequestParam(required = false) String keyword,
            Pageable pageable,
            Model model
    ) {
        Page<MentoringOfferResponse> result = mentoringOfferService.searchOffers(keyword, pageable);
        model.addAttribute("offerPage", result);
        model.addAttribute("keyword", keyword); // 검색어도 뷰에 전달하고 싶다면
        return "classes/classList"; // 검색 결과를 보여줄 Thymeleaf 템플릿
    }

    // http://localhost:8080/class/myClassList
    @GetMapping("/myClassList")
    public String getMyClassList(@RequestParam(required = false) MatchRole role, Model model) {
        Integer userId = 1; // 임시 하드코딩

        // 1. matchMember 정보 가져오기
        List<MentoringMatchMemberResponse> memberofferList = (role != null)
                ? matchMemberService.findByUserIdAndRole(userId, role)
                : matchMemberService.findByUserId(userId);

        // 2. matchId만 추출
        List<Integer> matchIds = memberofferList.stream()
                .map(MentoringMatchMemberResponse::getMatchId)
                .toList();

        // 3. matchId로 매칭 정보 가져오기
        List<MentoringMatchResponse> matchList = matchService.findMatchesByMatchIds(matchIds);

        // 4. 사용자가 직접 등록한 수업 (offer)
        List<MentoringOfferResponse> offerList = mentoringOfferService.findByUserId(userId);

        // 5. 모델에 담기
        model.addAttribute("matchList", matchList);     // 내가 참여한 매칭 정보
        model.addAttribute("offerList", offerList);     // 내가 등록한 수업
        model.addAttribute("selectedRole", role);

        return "classes/myClassList";
    }

    @GetMapping("/myDetail/{offerId}")
    public String getMyClassDetail(@PathVariable Integer offerId, Model model){

        MentoringOfferResponse offer = mentoringOfferService.findById(offerId);
        model.addAttribute("offer", offer);

        return "classes/myClassDetail";
    }

}
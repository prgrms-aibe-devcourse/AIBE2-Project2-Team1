package com.example.campy.controller;

import com.example.campy.dto.review.request.ReviewCreateRequest;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.user.response.UserResponseDto;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.dto.talent.response.TalentResponseDto;
import com.example.campy.dto.board.response.BoardResponseDto;
import com.example.campy.dto.material.response.MaterialResponseDto;
import com.example.campy.dto.material.request.MaterialUpdateRequest;
import com.example.campy.dto.board.request.BoardUpdateRequest;
import com.example.campy.dto.talent.request.TalentUpdateRequest;
import com.example.campy.dto.talent.request.TalentCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferUpdateRequest;
import com.example.campy.service.AdminService;
import com.example.campy.service.UserReviewService;
import com.example.campy.service.MentoringOfferService;
import com.example.campy.service.TalentService;
import com.example.campy.service.BoardService;
import com.example.campy.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserReviewService userReviewService;
    private final AdminService adminService; // 사용자 목록을 가져오기 위해 주입
    private final MentoringOfferService mentoringOfferService;
    private final TalentService talentService;
    private final BoardService boardService;
    private final MaterialService materialService;

    // 계정 관리
    @GetMapping("/account/profile")
    public String profilePage() {
        return "mypage/account/profile";
    }

    @GetMapping("/account/academic")
    public String academicPage() {
        return "mypage/account/academic";
    }

    // 내 활동 관리
    @GetMapping("/activity/registrations")
    public String registrationsPage(Authentication authentication, Model model) {
        List<TalentResponseDto> talents = talentService.getAllTalentsByUser(authentication);
        Integer userId = ((com.example.campy.service.CustomUserDetails) authentication.getPrincipal()).getUserId();
        List<MentoringOfferResponse> mentoringOffers = mentoringOfferService.findByUserId(userId);
        List<ReviewResponseDto> reviews = userReviewService.getAllReviewsByUser(authentication);
        List<BoardResponseDto> boards = boardService.getAllBoardsByUser(authentication);
        List<MaterialResponseDto> materials = materialService.getAllMaterialsByUser(authentication);

        model.addAttribute("talents", talents);
        model.addAttribute("mentoringOffers", mentoringOffers);
        model.addAttribute("reviews", reviews);
        model.addAttribute("boards", boards);
        model.addAttribute("materials", materials);

        return "mypage/mypage_activity/registrations";
    }

    @GetMapping("/activity/purchases")
    public String purchasesPage() {
        return "mypage/activity/purchases";
    }

    // 기존 리뷰 관련 기능 (경로 조정)
    @GetMapping("/activity/reviews/myreviews")
    public String getMyReviews(Authentication authentication, Model model) {
        List<ReviewResponseDto> reviews = userReviewService.getAllReviewsByUser(authentication);
        model.addAttribute("reviews", reviews);
        return "mypage/activity/mypage_review/mypage_reviews";
    }

    @GetMapping("/activity/reviews/new")
    public String createReviewForm(Model model) {
        model.addAttribute("reviewCreateRequest", new ReviewCreateRequest());
        List<UserResponseDto> users = adminService.getAllUsers(); // 리뷰 대상 사용자 선택을 위해 모든 사용자 목록 제공
        model.addAttribute("users", users);
        return "mypage/activity/mypage_review/mypage_review_new";
    }

    @PostMapping("/activity/reviews/new")
    public String createReview(@Valid @ModelAttribute ReviewCreateRequest request, BindingResult bindingResult, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<UserResponseDto> users = adminService.getAllUsers();
            model.addAttribute("users", users);
            return "mypage/activity/mypage_review/mypage_review_new";
        }
        userReviewService.createReview(request, authentication);
        redirectAttributes.addFlashAttribute("message", "후기가 성공적으로 작성되었습니다.");
        return "redirect:/mypage/activity/reviews/myreviews";
    }

    @GetMapping("/activity/reviews/{reviewId}/edit")
    public String editReviewForm(@PathVariable Integer reviewId, Authentication authentication, Model model) {
        ReviewResponseDto review = userReviewService.getReviewById(reviewId, authentication);
        System.out.println("review: " + review);
        ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest(review.rating(), review.category(), review.content());
        System.out.println("reviewUpdateRequest: " + reviewUpdateRequest);
        model.addAttribute("review", review);
        model.addAttribute("reviewUpdateRequest", reviewUpdateRequest);
        return "mypage/activity/mypage_review/mypage_review_edit";
    }

    @PostMapping("/activity/reviews/{reviewId}/update")
    public String updateReview(@PathVariable Integer reviewId, @ModelAttribute ReviewUpdateRequest request, Authentication authentication, RedirectAttributes redirectAttributes) {
        userReviewService.updateReview(reviewId, request, authentication);
        redirectAttributes.addFlashAttribute("message", "후기가 성공적으로 수정되었습니다.");
        return "redirect:/mypage/activity/reviews/myreviews";
    }

    @PostMapping("/activity/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Integer reviewId, Authentication authentication, RedirectAttributes redirectAttributes) {
        userReviewService.deleteReview(reviewId, authentication);
        redirectAttributes.addFlashAttribute("message", "후기가 성공적으로 삭제되었습니다.");
        return "redirect:/mypage/activity/reviews/myreviews";
    }

    // 재능 수정
    @GetMapping("/activity/talents/{talentId}/edit")
    public String editTalentForm(@PathVariable Integer talentId, Authentication authentication, Model model) {
        TalentResponseDto talent = talentService.getTalentById(talentId);
        TalentUpdateRequest talentUpdateRequest = new TalentUpdateRequest();
        talentUpdateRequest.setTalentId(talent.getTalentId());
        talentUpdateRequest.setTitle(talent.getTitle());
        talentUpdateRequest.setDescription(talent.getDescription());
        talentUpdateRequest.setPrice(talent.getPrice());
        talentUpdateRequest.setAvailableDays(talent.getAvailableDays());
        talentUpdateRequest.setOfflineLocation(talent.getOfflineLocation());
        talentUpdateRequest.setCategory(talent.getCategory());
        talentUpdateRequest.setTagNames(talent.getTagNames());

        model.addAttribute("talent", talent);
        model.addAttribute("talentUpdateRequest", talentUpdateRequest);
        return "mypage/mypage_activity/mypage_talent/mypage_talent_edit";
    }

    @PostMapping("/activity/talents/{talentId}/update")
    public String updateTalent(@PathVariable Integer talentId,
                               @ModelAttribute TalentUpdateRequest request,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) throws IOException {
        talentService.updateTalent(talentId, request, image, authentication);
        redirectAttributes.addFlashAttribute("message", "재능이 성공적으로 수정되었습니다.");
        return "redirect:/mypage/activity/registrations";
    }

    

    // 멘토링 제안 수정
    @GetMapping("/activity/mentoringoffers/{offerId}/edit")
    public String editMentoringOfferForm(@PathVariable Integer offerId, Authentication authentication, Model model) {
        Integer userId = ((com.example.campy.service.CustomUserDetails) authentication.getPrincipal()).getUserId();
        MentoringOfferResponse offer = mentoringOfferService.findById(offerId);
        model.addAttribute("mentoringOffer", offer);
        return "mypage/mypage_activity/mypage_mentoringoffer/mypage_mentoringoffer_edit";
    }



    // 재능 삭제
    @PostMapping("/activity/talents/{talentId}/delete")
    public String deleteTalent(@PathVariable Integer talentId, Authentication authentication, RedirectAttributes redirectAttributes) {
        talentService.deleteTalent(talentId, authentication);
        redirectAttributes.addFlashAttribute("message", "재능이 성공적으로 삭제되었습니다.");
        return "redirect:/mypage/activity/registrations";
    }

    

    // 게시물 수정
    @GetMapping("/activity/boards/{boardId}/edit")
    public String editBoardForm(@PathVariable Integer boardId, Authentication authentication, Model model) {
        BoardResponseDto board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);
        return "mypage/mypage_activity/mypage_board/mypage_board_edit";
    }

    @PostMapping("/activity/boards/{boardId}/update")
    public String updateBoard(@PathVariable Integer boardId,
                              @ModelAttribute BoardUpdateRequest request,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        boardService.updateBoard(boardId, request, authentication);
        redirectAttributes.addFlashAttribute("message", "게시물이 성공적으로 수정되었습니다.");
        return "redirect:/mypage/activity/registrations";
    }

    @PostMapping("/activity/boards/{boardId}/delete")
    public String deleteBoard(@PathVariable Integer boardId, Authentication authentication, RedirectAttributes redirectAttributes) {
        boardService.deleteBoard(boardId, authentication);
        redirectAttributes.addFlashAttribute("message", "게시물이 성공적으로 삭제되었습니다.");
        return "redirect:/mypage/activity/registrations";
    }

    // 새 재능 등록 폼
    @GetMapping("/activity/talents/new")
    public String createTalentForm(Model model) {
        model.addAttribute("talentCreateRequest", new TalentCreateRequest());
        return "mypage/mypage_activity/mypage_talent/mypage_talent_new";
    }

    // 새 재능 등록 처리
    @PostMapping("/activity/talents/new")
    public String createTalent(@Valid @ModelAttribute TalentCreateRequest request,
                               BindingResult bindingResult,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "mypage/mypage_activity/mypage_talent/mypage_talent_new";
        }
        talentService.createTalent(request, image, authentication);
        redirectAttributes.addFlashAttribute("message", "재능이 성공적으로 등록되었습니다.");
        return "redirect:/mypage/activity/registrations";
    }

    // 자료 수정
    @GetMapping("/activity/materials/{materialId}/edit")
    public String editMaterialForm(@PathVariable Integer materialId, Authentication authentication, Model model) {
        MaterialResponseDto material = materialService.getMaterialById(materialId);
        System.out.println("Editing material with ID: " + materialId);
        MaterialUpdateRequest materialUpdateRequest = new MaterialUpdateRequest(
                material.materialId(),
                material.title(),
                material.content(),
                material.price(),
                material.isDeleted()
        );
        System.out.println("MaterialUpdateRequest materialId: " + materialUpdateRequest.materialId());
        model.addAttribute("material", material);
        model.addAttribute("materialUpdateRequest", materialUpdateRequest);
        return "mypage/mypage_activity/mypage_material/mypage_material_edit";
    }

    @PostMapping("/activity/materials/{materialId}/update")
    public String updateMaterial(@PathVariable Integer materialId,
                                 @ModelAttribute MaterialUpdateRequest request,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        materialService.updateMaterial(materialId, request, authentication);
        redirectAttributes.addFlashAttribute("message", "자료가 성공적으로 수정되었습니다.");
        return "redirect:/mypage/activity/registrations";
    }

    @PostMapping("/activity/materials/{materialId}/delete")
    public String deleteMaterial(@PathVariable Integer materialId, Authentication authentication, RedirectAttributes redirectAttributes) {
        materialService.deleteMaterial(materialId, authentication);
        redirectAttributes.addFlashAttribute("message", "자료가 성공적으로 삭제되었습니다.");
        return "redirect:/mypage/activity/registrations";
    }

    // 새 멘토링 제안 등록 폼
    @GetMapping("/activity/mentoringoffers/new")
    public String createMentoringOfferForm(Model model) {
        model.addAttribute("mentoringOfferCreateRequest", new MentoringOfferCreateRequest());
        return "mypage/mypage_activity/mypage_mentoringoffer/mypage_mentoringoffer_new";
    }

    // 새 멘토링 제안 등록 처리
    @PostMapping("/activity/mentoringoffers/new")
    public String createMentoringOffer(@Valid @ModelAttribute MentoringOfferCreateRequest request,
                                       BindingResult bindingResult,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "mypage/mypage_activity/mypage_mentoringoffer/mypage_mentoringoffer_new";
        }
        Integer userId = ((com.example.campy.service.CustomUserDetails) authentication.getPrincipal()).getUserId();
        mentoringOfferService.create(request, userId);
        redirectAttributes.addFlashAttribute("message", "멘토링 제안이 성공적으로 등록되었습니다.");
        return "redirect:/mypage/activity/registrations";
    }
}

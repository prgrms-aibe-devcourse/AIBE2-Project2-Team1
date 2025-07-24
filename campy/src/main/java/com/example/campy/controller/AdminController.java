package com.example.campy.controller;

import com.example.campy.service.AdminService;
import com.example.campy.service.BoardService;
import com.example.campy.dto.review.response.ReviewResponseDto;
import com.example.campy.dto.review.request.ReviewUpdateRequest;
import com.example.campy.dto.review.request.ReviewCreateRequest;
import com.example.campy.dto.user.response.UserResponseDto;
import com.example.campy.dto.user.request.UserUpdateRequest;
import com.example.campy.dto.user.request.UserCreateRequest; // UserCreateRequest import 추가
import com.example.campy.dto.board.response.BoardResponseDto;
import com.example.campy.dto.board.request.BoardUpdateRequest;
import com.example.campy.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final BoardService boardService;

    @GetMapping
    public String adminPage(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            adminRepository.findByUsername(username).ifPresent(admin -> {
                model.addAttribute("admin", admin);
            });
        }
        return "admin/admin";
    }

    @GetMapping("/reviews")
    public String getReviews(@RequestParam(value = "searchType", required = false) String searchType,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             Model model) {
        List<ReviewResponseDto> reviews = adminService.getAllReviews(searchType, keyword);
        model.addAttribute("reviews", reviews);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "admin/admin_reviews/admin_reviews";
    }

    @GetMapping("/reviews/new")
    public String createReviewForm(Model model) {
        model.addAttribute("reviewCreateRequest", new ReviewCreateRequest(null, null, null, null, null, null));
        List<UserResponseDto> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin_reviews/admin_review_new";
    }

    @PostMapping("/reviews/new")
    public String createReview(@Valid @ModelAttribute ReviewCreateRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<UserResponseDto> users = adminService.getAllUsers();
            model.addAttribute("users", users);
            return "admin/admin_reviews/admin_review_new";
        }
        adminService.createReview(request);
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 생성되었습니다.");
        return "redirect:/admin/reviews";
    }

    @GetMapping("/reviews/{reviewId}/edit")
    public String editReviewForm(@PathVariable Integer reviewId, Model model) {
        ReviewResponseDto review = adminService.getReviewById(reviewId);
        model.addAttribute("review", review);
        return "admin/admin_reviews/admin_review_edit";
    }

    @PostMapping("/reviews/{reviewId}/update")
    public String updateReview(@PathVariable Integer reviewId, @ModelAttribute ReviewUpdateRequest request, RedirectAttributes redirectAttributes) {
        adminService.updateReview(reviewId, request);
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 수정되었습니다.");
        return "redirect:/admin/reviews";
    }

    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Integer reviewId, RedirectAttributes redirectAttributes) {
        adminService.deleteReview(reviewId);
        redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 삭제되었습니다.");
        return "redirect:/admin/reviews";
    }


    @GetMapping("/users")
    public String adminUsersPage(Model model) {
        List<UserResponseDto> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin_users/admin_users";
    }

    // 회원 생성 폼을 보여주는 GET 엔드포인트 추가
    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        model.addAttribute("userCreateRequest", new UserCreateRequest(null, null, null, null, null, null, null, null)); // 빈 DTO 전달
        return "admin/admin_users/admin_user_new";
    }

    // 회원 생성 폼 제출을 처리하는 POST 엔드포인트 추가
    @PostMapping("/users/new")
    public String createUser(@Valid @ModelAttribute UserCreateRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/admin_users/admin_user_new";
        }
        adminService.createUser(request);
        redirectAttributes.addFlashAttribute("message", "회원이 성공적으로 생성되었습니다.");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{userId}/edit")
    public String editUserForm(@PathVariable Integer userId, Model model) {
        UserResponseDto userResponseDto = adminService.getUserById(userId);
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .username(userResponseDto.username() != null ? userResponseDto.username() : "")
                .email(userResponseDto.email() != null ? userResponseDto.email() : "")
                .name(userResponseDto.name() != null ? userResponseDto.name() : "")
                .nickname(userResponseDto.nickname() != null ? userResponseDto.nickname() : "")
                .major(userResponseDto.major())
                .school(userResponseDto.school())
                .entranceYear(userResponseDto.entranceYear())
                .role(userResponseDto.role() != null ? userResponseDto.role() : "")
                .isVerified(userResponseDto.isVerified() != null ? userResponseDto.isVerified() : false)
                .profileImg(userResponseDto.profileImg())
                .intro(userResponseDto.intro())
                .build();
        model.addAttribute("user", userResponseDto);
        model.addAttribute("userUpdateRequest", userUpdateRequest);
        return "admin/admin_users/admin_user_edit";
    }

    @PostMapping("/users/{userId}/update")
    public String updateUser(@PathVariable Integer userId, @Valid @ModelAttribute UserUpdateRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            System.out.println("Validation Errors:");
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("user", adminService.getUserById(userId));
            model.addAttribute("userUpdateRequest", request);
            return "admin/admin_users/admin_user_edit";
        }
        adminService.updateUser(userId, request);
        redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable Integer userId, RedirectAttributes redirectAttributes) {
        adminService.deleteUser(userId);
        redirectAttributes.addFlashAttribute("message", "회원이 성공적으로 삭제되었습니다.");
        return "redirect:/admin/users";
    }

    @GetMapping("/boards")
    public String adminBoardsPage(Model model) {
        List<BoardResponseDto> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "admin/admin_boards/admin_boards";
    }

    @GetMapping("/boards/{boardId}/edit")
    public String editBoardForm(@PathVariable Integer boardId, Model model) {
        BoardResponseDto board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);
        BoardUpdateRequest boardUpdateRequest = BoardUpdateRequest.builder()
                .school(board.school())
                .category(board.category())
                .title(board.title())
                .content(board.content())
                .isDeleted(board.isDeleted())
                .build();
        model.addAttribute("boardUpdateRequest", boardUpdateRequest);
        return "admin/admin_boards/admin_board_edit";
    }

    @PostMapping("/boards/{boardId}/update")
    public String updateBoard(@PathVariable Integer boardId, @Valid @ModelAttribute BoardUpdateRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("board", boardService.getBoardById(boardId));
            model.addAttribute("boardUpdateRequest", request);
            return "admin/admin_boards/admin_board_edit";
        }
        boardService.updateBoard(boardId, request);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 수정되었습니다.");
        return "redirect:/admin/boards";
    }

    @PostMapping("/boards/{boardId}/delete")
    public String deleteBoard(@PathVariable Integer boardId, RedirectAttributes redirectAttributes) {
        boardService.deleteBoard(boardId);
        redirectAttributes.addFlashAttribute("message", "게시글이 성공적으로 삭제되었습니다.");
        return "redirect:/admin/boards";
    }

    @GetMapping("/talents")
    public String adminTalentsPage() {
        return "admin/admin_talents/admin_talents";
    }

    @GetMapping("/mentoring-offers")
    public String adminMentoringOffersPage() {
        return "admin/admin_mentoring_offers/admin_mentoring_offers";
    }

    @GetMapping("/mentoring-matches")
    public String adminMentoringMatchesPage() {
        return "admin/admin_mentoring_matches/admin_mentoring_matches";
    }

    @GetMapping("/transactions")
    public String adminTransactionsPage() {
        return "admin/admin_transactions/admin_transactions";
    }

    @GetMapping("/ai-stats")
    public String adminAiStatsPage() {
        return "admin/admin_ai_stats/admin_ai_stats";
    }
}
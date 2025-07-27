package com.example.campy.controller;

import com.example.campy.dto.board.request.BoardCreateRequest;
import com.example.campy.dto.board.response.BoardResponseDto;
import com.example.campy.entity.User;
import com.example.campy.repository.UserRepository;
import com.example.campy.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;

    @GetMapping("/listBoard")
    public String listBoard(Model model) {
        List<BoardResponseDto> boardList = boardService.getAllBoards();
        model.addAttribute("boardList", boardList);
        model.addAttribute("totalPages", 1); // 임시로 1로 설정
        model.addAttribute("currentPage", 1); // 임시로 1로 설정
        model.addAttribute("hasPrevious", false); // 임시
        model.addAttribute("hasNext", false); // 임시
        return "board/listBoard";
    }

    @GetMapping("/readBoard/{id}")
    public String readBoard(@PathVariable Integer id, Model model) {
        BoardResponseDto board = boardService.getBoardById(id);
        model.addAttribute("post", board);
        return "board/readBoard";
    }

    @GetMapping("/writeBoard")
    public String writeBoardForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            userRepository.findByUsername(username).ifPresent(user -> {
                model.addAttribute("user", user);
            });
        }
        return "board/writeBoard";
    }

    @PostMapping("/writeBoard")
    public String writeBoard(BoardCreateRequest request, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
            boardService.createBoard(new BoardCreateRequest(
                    currentUser.getUserId(),
                    currentUser.getSchool(),
                    request.category(),
                    request.title(),
                    request.content()
            ));
        }
        return "redirect:/board/listBoard";
    }

    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Integer id, Authentication authentication) {
        boardService.deleteBoard(id, authentication);
        return "redirect:/board/listBoard";
    }
}

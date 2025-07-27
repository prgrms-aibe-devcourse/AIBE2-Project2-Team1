package com.example.campy.controller;

import com.example.campy.dto.board.response.BoardResponseDto;
import com.example.campy.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

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
}

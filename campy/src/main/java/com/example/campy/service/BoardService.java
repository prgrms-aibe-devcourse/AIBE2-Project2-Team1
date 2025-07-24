package com.example.campy.service;

import com.example.campy.entity.Board;
import com.example.campy.repository.BoardRepository;
import com.example.campy.dto.board.request.BoardUpdateRequest;
import com.example.campy.dto.board.response.BoardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 모든 게시글 조회 (isDeleted가 false인 게시글만)
    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findByIsDeletedFalse().stream() // 변경된 부분
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 게시글 ID로 조회
    public BoardResponseDto getBoardById(Integer boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        return new BoardResponseDto(board);
    }

    // 게시글 업데이트
    @Transactional
    public void updateBoard(Integer boardId, BoardUpdateRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));

        board.setSchool(request.school());
        board.setCategory(request.category());
        board.setTitle(request.title());
        board.setContent(request.content());
        board.setIsDeleted(request.isDeleted());
        board.setUpdatedAt(LocalDateTime.now());

        boardRepository.save(board);
    }

    // 게시글 삭제 (soft delete)
    @Transactional
    public void deleteBoard(Integer boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        board.setIsDeleted(true); // isDeleted 필드를 true로 설정
        board.setUpdatedAt(LocalDateTime.now());
        boardRepository.save(board);
    }
}
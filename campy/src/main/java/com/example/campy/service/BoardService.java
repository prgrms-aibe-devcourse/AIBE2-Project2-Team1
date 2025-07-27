package com.example.campy.service;

import com.example.campy.dto.board.request.BoardCreateRequest;
import com.example.campy.dto.board.request.BoardUpdateRequest;
import com.example.campy.dto.board.response.BoardResponseDto;
import com.example.campy.entity.Board;
import com.example.campy.entity.User;
import com.example.campy.repository.BoardRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardResponseDto createBoard(BoardCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + request.userId()));
        Board board = request.toEntity(user);
        Board savedBoard = boardRepository.save(board);
        return BoardResponseDto.from(savedBoard);
    }

    public BoardResponseDto getBoardById(Integer boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + boardId));
        return BoardResponseDto.from(board);
    }

    @Transactional
    public BoardResponseDto adminUpdateBoard(Integer boardId, BoardUpdateRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + boardId));
        board.setSchool(request.school());
        board.setCategory(request.category());
        board.setTitle(request.title());
        board.setContent(request.content());
        board.setIsDeleted(request.isDeleted());
        Board updatedBoard = boardRepository.save(board);
        return BoardResponseDto.from(updatedBoard);
    }

    @Transactional
    public void adminDeleteBoard(Integer boardId) {
        boardRepository.deleteById(boardId);
    }

    public List<BoardResponseDto> getAllBoardsByUser(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return boardRepository.findByUser(currentUser).stream()
                .map(BoardResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardResponseDto updateBoard(Integer boardId, BoardUpdateRequest request, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + boardId));

        if (!board.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("You are not authorized to update this board.");
        }

        board.setSchool(request.school());
        board.setCategory(request.category());
        board.setTitle(request.title());
        board.setContent(request.content());
        board.setUpdatedAt(LocalDateTime.now());
        Board updatedBoard = boardRepository.save(board);
        return BoardResponseDto.from(updatedBoard);
    }

    @Transactional
    public void deleteBoard(Integer boardId, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + boardId));

        if (!board.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new IllegalArgumentException("You are not authorized to delete this board.");
        }

        boardRepository.delete(board);
    }
}

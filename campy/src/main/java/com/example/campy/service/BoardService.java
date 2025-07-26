package com.example.campy.service;

import com.example.campy.entity.Board;
import com.example.campy.entity.User; // User import 추가
import com.example.campy.repository.BoardRepository;
import com.example.campy.repository.UserRepository; // UserRepository import 추가
import com.example.campy.dto.board.request.BoardUpdateRequest;
import com.example.campy.dto.board.request.BoardCreateRequest; // BoardCreateRequest import 추가
import com.example.campy.dto.board.response.BoardResponseDto;
import com.example.campy.exception.GeneralException; // GeneralException import 추가
import com.example.campy.constant.ErrorCode; // ErrorCode import 추가
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication; // Authentication import 추가

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository; // UserRepository 주입

    // 모든 게시글 조회 (isDeleted가 false인 게시글만)
    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findByIsDeletedFalse().stream()
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
    public void updateBoard(Integer boardId, BoardUpdateRequest request, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다: " + username));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));

        if (!board.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED, "게시글을 수정할 권한이 없습니다.");
        }

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
    public void deleteBoard(Integer boardId, Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다: " + username));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));

        if (!board.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new GeneralException(ErrorCode.UNAUTHORIZED, "게시글을 삭제할 권한이 없습니다.");
        }

        board.setIsDeleted(true); // isDeleted 필드를 true로 설정
        board.setUpdatedAt(LocalDateTime.now());
        boardRepository.save(board);
    }

    // 새로운 게시글 생성 메서드 추가
    @Transactional
    public BoardResponseDto createBoard(BoardCreateRequest request) {
        User author = userRepository.findById(request.userId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "작성자 사용자를 찾을 수 없습니다."));

        Board newBoard = Board.builder()
                .user(author)
                .school(request.school())
                .category(request.category())
                .title(request.title())
                .content(request.content())
                .isDeleted(false) // 생성 시 기본값 false
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Board savedBoard = boardRepository.save(newBoard);
        return new BoardResponseDto(savedBoard);
    }

    // 현재 로그인한 사용자가 작성한 게시글 조회
    public List<BoardResponseDto> getAllBoardsByUser(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다: " + username));

        return boardRepository.findByUserAndIsDeletedFalse(currentUser).stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }
}

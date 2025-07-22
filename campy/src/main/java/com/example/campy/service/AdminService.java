package com.example.campy.service;

import com.example.campy.repository.ReviewRepository;
import com.example.campy.dto.review.response.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ReviewRepository reviewRepository;

    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    // TODO: 특정 리뷰 조회, 리뷰 삭제 (소프트 삭제) 등 추가 예정
}

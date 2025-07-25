package com.example.campy.service;


import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.entity.User;
import com.example.campy.repository.UserRepository;
import com.example.campy.service.MentoringOfferService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("멘토링 제안 서비스 통합 테스트(디비)")
public class MentoringOfferIntegrationTest {

    @Autowired private MentoringOfferService mentoringOfferService;
    @Autowired private UserRepository userRepository;

    @DisplayName("유효한 요청과 유저 ID가 주어지면 실제 DB에 저장되고 Response를 반환한다.")
    @Test
    void createMentoringOffer_withValidRequest_persistsToDatabase() {
        // Given
        User user = User.builder()
                .nickname("테스트 유저")
                .username("test_user")
                .email("test@example.com")
                .password("1234")
                .build();
        userRepository.save(user);

        MentoringOfferCreateRequest req = MentoringOfferCreateRequest.builder()
                .title("Java 멘토링")
                .description("실제 DB 테스트")
                .tags(List.of("spring", "jpa"))
                .build();

        // When
        MentoringOfferResponse response = mentoringOfferService.create(req, user.getUserId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Java 멘토링");
    }
}
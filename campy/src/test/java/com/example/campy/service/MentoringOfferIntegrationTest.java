package com.example.campy.service;


import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.entity.User;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("멘토링 제안 서비스 통합 테스트(디비)")
public class MentoringOfferIntegrationTest {

    @Autowired private MentoringOfferService mentoringOfferService;
    @Autowired private UserRepository userRepository;

    // 공통 메소드
    private User createTestUser() {
        return userRepository.save(User.builder()
                .nickname("테스트 유저")
                .username("test_user")
                .email("test@example.com")
                .password("1234")
                .build());
    }

    private MentoringOfferCreateRequest createOfferRequest() {
        return MentoringOfferCreateRequest.builder()
                .title("Java 멘토링")
                .description("실제 DB 테스트")
                .tags(List.of("spring", "jpa"))
                .build();
    }

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

    @DisplayName("offerId로 조회하면 해당 제안을 반환한다.")
    @Test
    void findMentoringOfferById_returnsCorrectData() {
        // Given
        User user = createTestUser();

        MentoringOfferCreateRequest req = MentoringOfferCreateRequest.builder()
                .title("Java 멘토링")
                .description("실제 DB 테스트")
                .tags(List.of("spring", "jpa"))
                .build();

        MentoringOfferResponse created = mentoringOfferService.create(req, user.getUserId());
        Integer offerId = created.getOfferId();
        List<String> expectedTagNames = List.of("spring", "jpa");

        // When
        MentoringOfferResponse result = mentoringOfferService.findById(offerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOfferId()).isEqualTo(offerId);
        assertThat(result.getTitle()).isEqualTo("Java 멘토링");
        assertThat(result.getTags()).containsExactlyInAnyOrderElementsOf(expectedTagNames);
    }

    @DisplayName("저장된 제안을 삭제하면 조회 시 예외가 발생한다.")
    @Test
    void deleteMentoringOffer_setsIsDeletedTrue() {
        // Given
        User user = createTestUser();
        MentoringOfferCreateRequest req = createOfferRequest();
        MentoringOfferResponse created = mentoringOfferService.create(req, user.getUserId());

        // When
        mentoringOfferService.delete(created.getOfferId());

        // Then
        assertThrows(GeneralException.class, () ->
                mentoringOfferService.findById(created.getOfferId())
        );
    }

    @DisplayName("여러 개의 제안을 저장한 후 전체 조회하면 페이징된 결과가 반환된다.")
    @Test
    void findAllMentoringOffers_returnsPagedResults() {
        // Given
        User user = createTestUser();

        for (int i = 1; i <= 3; i++) {
            MentoringOfferCreateRequest req = MentoringOfferCreateRequest.builder()
                    .title("제안 " + i)
                    .description("설명 " + i)
                    .tags(List.of("tag" + i))
                    .build();
            mentoringOfferService.create(req, user.getUserId());
        }

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<MentoringOfferResponse> result = mentoringOfferService.findAll(pageable);

        // Then
        assertThat(result.getContent().size()).isGreaterThanOrEqualTo(3);
    }

    @DisplayName("키워드로 검색하면 해당 키워드가 포함된 제안들을 반환한다.")
    @Test
    void searchMentoringOffers_byKeyword_returnsMatchingResults() {
        // Given
        User user = createTestUser();

        MentoringOfferCreateRequest req = MentoringOfferCreateRequest.builder()
                .title("AI 스프링 부트 강의")
                .description("AI 입문자를 위한 과정")
                .tags(List.of("ai", "spring"))
                .build();

        mentoringOfferService.create(req, user.getUserId());

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<MentoringOfferResponse> result = mentoringOfferService.searchOffers("AI", pageable);

        // Then
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).getTitle()).contains("AI");
    }

    @DisplayName("태그로 검색하면 해당 태그가 포함된 제안들을 반환한다.")
    @Test
    void searchMentoringOffers_byTag_returnsMatchingResults() {
        // Given
        User user = createTestUser();

        MentoringOfferCreateRequest req = MentoringOfferCreateRequest.builder()
                .title("JPA 실습 강의")
                .description("JPA 활용법")
                .tags(List.of("jpa", "db"))
                .build();

        mentoringOfferService.create(req, user.getUserId());

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<MentoringOfferResponse> result = mentoringOfferService.searchByTag("jpa", pageable);

        // Then
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).getTags()).contains("jpa");
    }

    @DisplayName("상태값으로 조회하면 해당 상태의 제안만 반환된다.")
    @Test
    void findMentoringOffers_byStatus_returnsFilteredResults() {
        // Given
        User user = createTestUser();

        MentoringOfferCreateRequest req = MentoringOfferCreateRequest.builder()
                .title("상태 테스트")
                .description("진행중 상태")
                .tags(List.of("ongoing"))
                .build();

        MentoringOfferResponse created = mentoringOfferService.create(req, user.getUserId());

        MentoringOfferUpdateRequest updateReq = MentoringOfferUpdateRequest.builder()
                .title("업데이트")
                .description("업데이트 설명")
                .price(10000)
                .location("온라인")
                .maxParticipants(5)
                .duration(60)
                .status(MentoringStatus.ONGOING)
                .tags(List.of("ongoing"))
                .build();

        mentoringOfferService.update(created.getOfferId(), updateReq);

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<MentoringOfferResponse> result = mentoringOfferService.findByStatus(MentoringStatus.ONGOING, pageable);

        // Then
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(MentoringStatus.ONGOING.getLabel());
    }

    @DisplayName("사용자 ID로 해당 사용자의 멘토링 제안들을 반환한다.")
    @Test
    void findByUserId_returnsUserOffers() {
        // Given
        User user = createTestUser();

        for (int i = 1; i <= 2; i++) {
            MentoringOfferCreateRequest req = MentoringOfferCreateRequest.builder()
                    .title("사용자 제안 " + i)
                    .description("설명 " + i)
                    .tags(List.of("tag" + i))
                    .build();
            mentoringOfferService.create(req, user.getUserId());
        }

        // When
        List<MentoringOfferResponse> result = mentoringOfferService.findByUserId(user.getUserId());

        // Then
        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        assertThat(result.get(0).getTitle()).contains("사용자 제안");
    }



}
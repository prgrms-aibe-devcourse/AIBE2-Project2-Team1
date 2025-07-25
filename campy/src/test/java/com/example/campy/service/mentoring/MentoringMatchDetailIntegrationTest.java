package com.example.campy.service.mentoring;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.constant.MentoringType;
import com.example.campy.dto.mentoring.request.MentoringMatchDetailCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchDetailResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringMatchDetail;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.User;
import com.example.campy.repository.MentoringMatchDetailRepository;
import com.example.campy.repository.MentoringMatchRepository;
import com.example.campy.repository.MentoringOfferRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.service.MentoringMatchDetailService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("멘토링 매칭 상세 통합 테스트(디비)")
public class MentoringMatchDetailIntegrationTest {

    @Autowired private MentoringMatchDetailService detailService;
    @Autowired private MentoringMatchDetailRepository detailRepo;
    @Autowired private MentoringMatchRepository matchRepo;
    @Autowired private MentoringOfferRepository offerRepo;
    @Autowired private UserRepository userRepo;

    // 공통 테스트 유저 생성
    private User createTestUser() {
        return userRepo.save(User.builder()
                .nickname("통합유저")
                .username("match_user")
                .email("match@example.com")
                .password("1234")
                .build());
    }

    // 오퍼 생성 및 저장
    private MentoringOffer createTestOffer(User user) {
        return offerRepo.save(MentoringOffer.builder()
                .user(user)
                .title("오퍼 제목")
                .description("오퍼 설명")
                .status(MentoringStatus.REQUESTED)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build());
    }

    // 매칭 생성 (오퍼 연결)
    private MentoringMatch createTestMatch(User user) {
        MentoringOffer offer = createTestOffer(user);

        return matchRepo.save(MentoringMatch.builder()
                .mentoringOffer(offer)
                .status(MentoringStatus.WAITING_FOR_MENTOR)
                .type(MentoringType.INDIVIDUAL)
                .createdAt(LocalDateTime.now())
                .build());
    }

    // 매칭 상세 요청 DTO
    private MentoringMatchDetailCreateRequest createTestRequest() {
        return MentoringMatchDetailCreateRequest.builder()
                .topic("통합 테스트 주제")
                .summary("통합 테스트 요약입니다.")
                .sessionDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(1).plusHours(1))
                .durationMinutes(60)
                .build();
    }

    @DisplayName("create 요청 시 실제 DB에 저장된다")
    @Test
    void createDetail_savesToDatabase() {
        // Given
        User user = createTestUser();
        MentoringMatch match = createTestMatch(user);
        MentoringMatchDetailCreateRequest req = createTestRequest();

        // When
        detailService.createDetail(match, req);

        // Then
        MentoringMatchDetail saved = detailRepo.findAll().get(0);
        assertThat(saved).isNotNull();
        assertThat(saved.getTopic()).isEqualTo("통합 테스트 주제");
    }

    @DisplayName("findById는 실제 저장된 데이터를 반환한다")
    @Test
    void findDetailById_returnsSavedData() {
        // Given
        User user = createTestUser();
        MentoringMatch match = createTestMatch(user);
        MentoringMatchDetailCreateRequest req = createTestRequest();
        detailService.createDetail(match, req);

        MentoringMatchDetail saved = detailRepo.findAll().get(0);

        // When
        MentoringMatchDetailResponse res = detailService.findById(saved.getId());

        // Then
        assertThat(res).isNotNull();
        assertThat(res.getTopic()).isEqualTo("통합 테스트 주제");
        assertThat(res.getMatchId()).isEqualTo(match.getMatchId());
    }
}

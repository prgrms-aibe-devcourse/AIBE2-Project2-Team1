package com.example.campy.service;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.constant.MentoringType;
import com.example.campy.dto.mentoring.request.MentoringMatchUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.User;
import com.example.campy.repository.MentoringMatchRepository;
import com.example.campy.repository.MentoringOfferRepository;
import com.example.campy.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MentoringMatchIntegrationTest {

    @Autowired private MentoringMatchService matchService;
    @Autowired private MentoringMatchRepository matchRepo;
    @Autowired private MentoringOfferRepository offerRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private EntityManager em;

    private MentoringMatch createDummyMatch(MentoringStatus status) {
        String username = "hong" + System.currentTimeMillis(); // or UUID

        User user = userRepo.save(User.builder()
                .name("홍길동")
                .nickname("길동")
                .email("test@example.com")
                .username(username)
                .password("encoded_pw")
                .role("USER")
                .build());

        MentoringOffer offer = offerRepo.save(MentoringOffer.builder()
                .user(user)
                .title("테스트 제안")
                .description("이것은 테스트 설명입니다.")
                .status(MentoringStatus.REQUESTED)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .price(10000)
                .location("서울")
                .maxParticipants(5)
                .duration(60)
                .build());

        return matchRepo.save(MentoringMatch.builder()
                .mentoringOffer(offer)
                .status(status)
                .type(MentoringType.GROUP)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @DisplayName("DB 기반 전체 매칭 조회: 삭제 제외")
    @Test
    void findAll_shouldWorkWithRealDB() {
        createDummyMatch(MentoringStatus.ONGOING);

        Page<MentoringMatchResponse> result = matchService.findAll(PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isGreaterThan(0);
    }

    @DisplayName("ID로 매칭 단건 조회")
    @Test
    void findById_shouldReturnMatch() {
        MentoringMatch match = createDummyMatch(MentoringStatus.PENDING);
        var result = matchService.findById(match.getMatchId());

        assertThat(result).isNotNull();
        assertThat(result.getMatchId()).isEqualTo(match.getMatchId());
    }

    @DisplayName("상태별 조회 (DELETED 제외)")
    @Test
    void findByStatus_shouldFilterCorrectly() {
        createDummyMatch(MentoringStatus.COMPLETED);
        createDummyMatch(MentoringStatus.WAITING_FOR_MENTOR);

        Page<MentoringMatchResponse> result = matchService.findByStatus(MentoringStatus.COMPLETED, PageRequest.of(0, 10));
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(MentoringStatus.COMPLETED.getLabel());
    }

    @DisplayName("매칭 상태 업데이트")
    @Test
    void updateMatch_shouldChangeStatus() {
        MentoringMatch match = createDummyMatch(MentoringStatus.REQUESTED);
        matchService.updatematch(match.getMatchId(),
                MentoringMatchUpdateRequest.builder()
                        .status(MentoringStatus.ONGOING)
                        .build());

        em.flush(); em.clear();

        MentoringMatch updated = matchRepo.findById(match.getMatchId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(MentoringStatus.ONGOING);
    }

    @DisplayName("매칭 soft delete (상태 DELETED로 변경)")
    @Test
    void deleteMatch_shouldSoftDelete() {
        MentoringMatch match = createDummyMatch(MentoringStatus.PENDING);
        matchService.deleteMatch(match.getMatchId());

        em.flush(); em.clear();

        MentoringMatch deleted = matchRepo.findById(match.getMatchId()).orElseThrow();
        assertThat(deleted.getStatus()).isEqualTo(MentoringStatus.DELETED);
        assertThat(deleted.getDeletedAt()).isNotNull();
    }

}

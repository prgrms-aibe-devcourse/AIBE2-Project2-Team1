package com.example.campy.service.mentoring;

import com.example.campy.constant.MatchRole;
import com.example.campy.constant.MentoringStatus;
import com.example.campy.constant.MentoringType;
import com.example.campy.dto.mentoring.request.*;
import com.example.campy.dto.mentoring.response.MentoringMatchResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.User;
import com.example.campy.repository.MentoringMatchRepository;
import com.example.campy.repository.MentoringOfferRepository;
import com.example.campy.service.MentoringMatchDetailService;
import com.example.campy.service.MentoringMatchMemberService;
import com.example.campy.service.MentoringMatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("멘토링 매칭 서비스 단위 테스트")
class MentoringMatchServiceTest {

    @InjectMocks
    private MentoringMatchService matchService;

    @Mock private MentoringMatchRepository matchRepo;
    @Mock private MentoringOfferRepository offerRepo;
    @Mock private MentoringMatchDetailService detailService;
    @Mock private MentoringMatchMemberService memberService;



    private final User dummyUser = User.builder().userId(1).nickname("테스터").build();
    private final MentoringOffer dummyOffer = MentoringOffer.builder()
            .offerId(10)
            .user(dummyUser)
            .title("더미 제안")
            .status(MentoringStatus.REQUESTED)
            .isDeleted(false)
            .build();

    private MentoringMatch dummyMatch() {
        return MentoringMatch.builder()
                .matchId(100)
                .mentoringOffer(dummyOffer)
                .type(MentoringType.INDIVIDUAL)
                .status(MentoringStatus.WAITING_FOR_MENTOR)
                .build();
    }

    @DisplayName("매칭 및 상세정보와 멤버가 함께 생성되면, 최종 응답을 반환한다")
    @Test
    void createMatchWithDetail_createsMatchDetailAndMembers_andReturnsResponse() {
        // Given
        MentoringMatchCreateRequest matchReq = new MentoringMatchCreateRequest(1, MentoringType.INDIVIDUAL);
        MentoringMatchDetailCreateRequest detailReq = new MentoringMatchDetailCreateRequest(
                "주제", "요약",
                LocalDateTime.of(2025, 7, 26, 10, 0),
                LocalDateTime.of(2025, 7, 26, 11, 0),
                60
        );
        List<MentoringMatchMemberCreateRequest> memberReqs = List.of(
                new MentoringMatchMemberCreateRequest(1, MatchRole.MENTOR),
                new MentoringMatchMemberCreateRequest(2, MatchRole.MENTEE)
        );

        MentoringMatchCreateCombinedRequest combinedReq = new MentoringMatchCreateCombinedRequest();
        combinedReq.setMatchRequest(matchReq);
        combinedReq.setDetailRequest(detailReq);
        combinedReq.setMembers(memberReqs);

        MentoringOffer dummyOffer = MentoringOffer.builder()
                .offerId(1)
                .status(MentoringStatus.REQUESTED)
                .build();

        MentoringMatch dummyMatch = MentoringMatch.builder()
                .matchId(100)
                .mentoringOffer(dummyOffer)
                .status(MentoringStatus.WAITING_FOR_MENTOR)
                .type(MentoringType.INDIVIDUAL)
                .createdAt(LocalDateTime.now())
                .build();

        given(offerRepo.findById(1)).willReturn(Optional.of(dummyOffer));
        given(matchRepo.save(any(MentoringMatch.class)))
                .willAnswer(invocation -> {
                    MentoringMatch matchArg = invocation.getArgument(0);
                    matchArg.setMatchId(100); // ID mocking
                    return matchArg;
                });
        willDoNothing().given(detailService).createDetail(any(MentoringMatch.class), any(MentoringMatchDetailCreateRequest.class));
        willDoNothing().given(memberService).createMembers(any(MentoringMatch.class), anyList());

        // When
        MentoringMatchResponse result = matchService.createMatchWithDetail(combinedReq);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMatchId()).isEqualTo(100);
        assertThat(result.getMentoringOfferId()).isEqualTo(1);
        assertThat(result.getType()).isEqualTo("개인"); // getLabel()
        assertThat(result.getStatus()).isEqualTo("멘토 수락 대기중");

        then(offerRepo).should().findById(1);
        then(matchRepo).should().save(any(MentoringMatch.class));
        then(detailService).should().createDetail(any(MentoringMatch.class), any(MentoringMatchDetailCreateRequest.class));
        then(memberService).should().createMembers(any(MentoringMatch.class), anyList());
    }

    @DisplayName("전체 매칭 조회 시, 삭제된 매칭은 제외되고 반환된다")
    @Test
    void findAll_shouldReturnOnlyNotDeletedMatches() {
        MentoringMatch match = dummyMatch();
        Page<MentoringMatch> page = new PageImpl<>(List.of(match));

        given(matchRepo.findByStatusNot(eq(MentoringStatus.DELETED), any(Pageable.class))).willReturn(page);

        Page<MentoringMatchResponse> result = matchService.findAll(PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getMatchId()).isEqualTo(match.getMatchId());

        then(matchRepo).should().findByStatusNot(eq(MentoringStatus.DELETED), any(Pageable.class));
    }

    @DisplayName("ID로 매칭 조회 시, 삭제되지 않은 매칭을 반환한다")
    @Test
    void findById_shouldReturnMatch_whenNotDeleted() {
        MentoringMatch match = dummyMatch();
        given(matchRepo.findByMatchIdAndStatusNot(eq(100), eq(MentoringStatus.DELETED)))
                .willReturn(Optional.of(match));

        MentoringMatchResponse result = matchService.findById(100);

        assertThat(result).isNotNull();
        assertThat(result.getMatchId()).isEqualTo(100);
        assertThat(result.getStatus()).isEqualTo("멘토 수락 대기중");

        then(matchRepo).should().findByMatchIdAndStatusNot(100, MentoringStatus.DELETED);
    }

    @DisplayName("상태별 매칭 조회 시, 삭제된 매칭은 제외되고 주어진 상태만 반환된다")
    @Test
    void findByStatus_shouldReturnOnlyMatchesWithGivenStatus() {
        MentoringMatch match = dummyMatch();
        Page<MentoringMatch> page = new PageImpl<>(List.of(match));

        given(matchRepo.findByStatusAndStatusNot(eq(MentoringStatus.WAITING_FOR_MENTOR), eq(MentoringStatus.DELETED), any(Pageable.class)))
                .willReturn(page);

        Page<MentoringMatchResponse> result = matchService.findByStatus(MentoringStatus.WAITING_FOR_MENTOR, PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo("멘토 수락 대기중");

        then(matchRepo).should().findByStatusAndStatusNot(eq(MentoringStatus.WAITING_FOR_MENTOR), eq(MentoringStatus.DELETED), any(Pageable.class));
    }


    @DisplayName("매칭 상태 업데이트 시, 해당 매칭의 상태가 변경된다")
    @Test
    void updateMatch_shouldChangeStatus() {
        MentoringMatch match = dummyMatch();
        given(matchRepo.findById(100)).willReturn(Optional.of(match));

        MentoringMatchUpdateRequest req = MentoringMatchUpdateRequest.builder()
                .status(MentoringStatus.COMPLETED)
                .build();

        matchService.updatematch(100, req);

        assertThat(match.getStatus()).isEqualTo(MentoringStatus.COMPLETED);
        then(matchRepo).should().findById(100);
    }

    @DisplayName("매칭 삭제 시, 상태가 DELETED로 변경되고 삭제 시간이 기록된다")
    @Test
    void deleteMatch_shouldSetDeletedStatusAndTime() {
        MentoringMatch match = dummyMatch();
        given(matchRepo.findById(100)).willReturn(Optional.of(match));

        matchService.deleteMatch(100);

        assertThat(match.getStatus()).isEqualTo(MentoringStatus.DELETED);
        assertThat(match.getDeletedAt()).isNotNull();

        then(matchRepo).should().findById(100);
    }


}
package com.example.campy.service.mentoring;

import com.example.campy.constant.MatchRole;
import com.example.campy.constant.MentoringStatus;
import com.example.campy.constant.MentoringType;
import com.example.campy.dto.mentoring.request.MentoringMatchMemberCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchMemberResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.MentoringMatchMember;
import com.example.campy.entity.User;
import com.example.campy.repository.MentoringMatchMemberRepository;
import com.example.campy.repository.MentoringMatchRepository;
import com.example.campy.repository.MentoringOfferRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.service.MentoringMatchMemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("멘토링 매칭 멤버 통합 테스트(디비)")
public class MentoringMatchMemberIntegrationTest {

    @Autowired private MentoringMatchMemberService memberService;
    @Autowired private MentoringMatchMemberRepository memberRepo;
    @Autowired private MentoringMatchRepository matchRepo;
    @Autowired private MentoringOfferRepository offerRepo;
    @Autowired private UserRepository userRepo;

    private User createUser(String name) {
        return userRepo.save(User.builder()
                .username(name)
                .nickname(name)
                .email(name + "@test.com")
                .password("pw")
                .build());
    }

    private MentoringOffer createOffer(User user) {
        return offerRepo.save(MentoringOffer.builder()
                .user(user)
                .title("오퍼 제목")
                .description("오퍼 설명")
                .status(MentoringStatus.ONGOING)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build());
    }

    private MentoringMatch createMatch(MentoringOffer offer) {
        return matchRepo.save(MentoringMatch.builder()
                .mentoringOffer(offer)
                .status(MentoringStatus.WAITING_FOR_MENTOR)
                .type(MentoringType.INDIVIDUAL)
                .createdAt(LocalDateTime.now())
                .build());
    }

    private MentoringMatchMemberCreateRequest createMemberRequest(User user, MatchRole role) {
        return MentoringMatchMemberCreateRequest.builder()
                .userId(user.getUserId())
                .role(role)
                .build();
    }

    @DisplayName("createMembers로 실제 멤버가 DB에 저장된다")
    @Test
    void createMembers_savesToDatabase() {
        // Given
        User mentor = createUser("mentor1");
        User mentee = createUser("mentee1");

        MentoringMatch match = createMatch(createOffer(mentor));

        List<MentoringMatchMemberCreateRequest> reqs = List.of(
                createMemberRequest(mentor, MatchRole.MENTOR),
                createMemberRequest(mentee, MatchRole.MENTEE)
        );

        // When
        memberService.createMembers(match, reqs);

        // Then
        List<MentoringMatchMember> saved = memberRepo.findAll();
        assertThat(saved).hasSize(2);
        assertThat(saved).anyMatch(m -> m.getUser().getUserId().equals(mentor.getUserId()));
        assertThat(saved).anyMatch(m -> m.getUser().getUserId().equals(mentee.getUserId()));
    }

    @DisplayName("findByMatchId는 해당 매칭의 모든 멤버를 반환한다")
    @Test
    void findByMatchId_returnsMembers() {
        // Given
        User user = createUser("userX");
        MentoringMatch match = createMatch(createOffer(user));

        memberService.createMembers(match, List.of(
                createMemberRequest(user, MatchRole.MENTEE)
        ));

        // When
        List<MentoringMatchMemberResponse> res = memberService.findByMatchId(match.getMatchId());

        // Then
        assertThat(res).hasSize(1);
        assertThat(res.get(0).getRole()).isEqualTo(MatchRole.MENTEE);
    }

    @DisplayName("findByUserId는 해당 사용자의 모든 멤버 역할을 반환한다")
    @Test
    void findByUserId_returnsMemberships() {
        // Given
        User user = createUser("userZ");
        MentoringMatch match = createMatch(createOffer(user));

        memberService.createMembers(match, List.of(
                createMemberRequest(user, MatchRole.MENTOR)
        ));

        // When
        List<MentoringMatchMemberResponse> res = memberService.findByUserId(user.getUserId());

        // Then
        assertThat(res).hasSize(1);
        assertThat(res.get(0).getRole()).isEqualTo(MatchRole.MENTOR);
    }

    @DisplayName("findByMatchIdAndRole로 역할별 조회가 가능하다")
    @Test
    void findByMatchIdAndRole_returnsFilteredByRole() {
        // Given
        User mentor = createUser("mentor2");
        User mentee = createUser("mentee2");

        MentoringMatch match = createMatch(createOffer(mentor));

        memberService.createMembers(match, List.of(
                createMemberRequest(mentor, MatchRole.MENTOR),
                createMemberRequest(mentee, MatchRole.MENTEE)
        ));

        // When
        List<MentoringMatchMemberResponse> mentors = memberService.findByMatchIdAndRole(match.getMatchId(), MatchRole.MENTOR);

        // Then
        assertThat(mentors).hasSize(1);
        assertThat(mentors.get(0).getRole()).isEqualTo(MatchRole.MENTOR);
    }
}

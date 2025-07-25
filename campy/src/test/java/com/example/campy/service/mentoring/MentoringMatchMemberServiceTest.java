package com.example.campy.service.mentoring;

import com.example.campy.constant.MatchRole;
import com.example.campy.dto.mentoring.request.MentoringMatchMemberCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchMemberResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringMatchMember;
import com.example.campy.entity.User;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MentoringMatchMemberRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.service.MentoringMatchMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@DisplayName("멘토링 매칭 멤버 로직")
@ExtendWith(MockitoExtension.class)
class MentoringMatchMemberServiceTest {

    @InjectMocks
    private MentoringMatchMemberService sut;

    @Mock
    private MentoringMatchMemberRepository memberRepo;

    @Mock
    private UserRepository userRepo;

    private MentoringMatch match = MentoringMatch.builder().matchId(1).build();

    @DisplayName("유효한 요청으로 멤버를 생성하면 저장이 수행된다")
    @Test
    void givenValidRequest_whenCreating_thenSavesMember() {
        // Given
        User user = User.builder().userId(1).build();
        MentoringMatchMemberCreateRequest req = MentoringMatchMemberCreateRequest.builder()
                .userId(1)
                .role(MatchRole.MENTEE)
                .build();

        given(userRepo.findById(1)).willReturn(Optional.of(user));

        // When
        sut.createMembers(match, List.of(req));

        // Then
        then(memberRepo).should().save(any(MentoringMatchMember.class));
    }

    @DisplayName("존재하지 않는 사용자로 생성 시 예외 발생")
    @Test
    void givenInvalidUser_whenCreating_thenThrowsException() {
        // Given
        MentoringMatchMemberCreateRequest req = MentoringMatchMemberCreateRequest.builder()
                .userId(999)
                .role(MatchRole.MENTOR)
                .build();

        given(userRepo.findById(999)).willReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> sut.createMembers(match, List.of(req)))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining("사용자가 존재하지 않습니다.");
    }

    @DisplayName("findAll()은 전체 멤버 응답 리스트를 반환한다")
    @Test
    void findAll_returnsAllMembers() {
        // Given
        MentoringMatchMember member = MentoringMatchMember.builder()
                .id(1)
                .user(User.builder().userId(1).build())
                .mentoringMatch(match)
                .role(MatchRole.MENTOR)
                .joinedAt(LocalDateTime.now())
                .build();

        given(memberRepo.findAll()).willReturn(List.of(member));

        // When
        List<MentoringMatchMemberResponse> result = sut.findAll();

        // Then
        assertThat(result).hasSize(1);
        then(memberRepo).should().findAll();
    }

    @DisplayName("삭제 요청 시 존재하지 않으면 예외 발생")
    @Test
    void givenInvalidId_whenDeleting_thenThrowsException() {
        // Given
        given(memberRepo.existsById(123)).willReturn(false);

        // When / Then
        assertThatThrownBy(() -> sut.delete(123))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining("매칭 멤버가 존재하지 않습니다.");
    }

    @DisplayName("유효한 ID로 삭제 요청 시 삭제가 수행된다")
    @Test
    void givenValidId_whenDeleting_thenDeletesMember() {
        // Given
        given(memberRepo.existsById(1)).willReturn(true);

        // When
        sut.delete(1);

        // Then
        then(memberRepo).should().deleteById(1);
    }
}

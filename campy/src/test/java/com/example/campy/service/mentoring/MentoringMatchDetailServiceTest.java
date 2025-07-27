package com.example.campy.service.mentoring;

import com.example.campy.constant.ErrorCode;
import com.example.campy.dto.mentoring.request.MentoringMatchDetailCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringMatchDetailUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringMatchDetailResponse;
import com.example.campy.entity.MentoringMatch;
import com.example.campy.entity.MentoringMatchDetail;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MentoringMatchDetailRepository;
import com.example.campy.service.MentoringMatchDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("멘토링 매칭 상세 로직")
@ExtendWith(MockitoExtension.class)

public class MentoringMatchDetailServiceTest {

    @InjectMocks
    private MentoringMatchDetailService sut;

    @Mock
    private MentoringMatchDetailRepository detailRepo;

    private MentoringMatchDetailCreateRequest createRequest() {
        return MentoringMatchDetailCreateRequest.builder()
                .topic("Java 기초")
                .summary("OOP 개념 정리")
                .sessionDate(LocalDateTime.of(2025, 7, 25, 10, 0))
                .endDate(LocalDateTime.of(2025, 7, 25, 11, 0))
                .durationMinutes(60)
                .build();
    }

    private MentoringMatchDetailUpdateRequest updateRequest() {
        return MentoringMatchDetailUpdateRequest.builder()
                .topic("Spring 심화")
                .summary("DI, AOP 정리")
                .sessionDate(LocalDateTime.of(2025, 7, 26, 14, 0))
                .endDate(LocalDateTime.of(2025, 7, 26, 15, 30))
                .durationMinutes(90)
                .build();
    }

    @DisplayName("create 요청이 오면 저장을 수행한다")
    @Test
    void givenCreateRequest_whenCreatingDetail_thenSavesDetail() {
        // Given
        MentoringMatch match = MentoringMatch.builder().matchId(1).build();
        MentoringMatchDetailCreateRequest req = createRequest();

        // When
        sut.createDetail(match, req);

        // Then
        then(detailRepo).should().save(any(MentoringMatchDetail.class));
    }

    @DisplayName("ID로 매칭 상세 조회하면 응답을 반환한다")
    @Test
    void givenId_whenFindingDetail_thenReturnsResponse() {
        MentoringMatch match = MentoringMatch.builder().matchId(1).build();
        // Given
        MentoringMatchDetail detail = MentoringMatchDetail.builder()
                .id(1)
                .mentoringMatch(match)
                .topic("Java 기초")
                .summary("OOP 개념 정리")
                .sessionDate(LocalDateTime.of(2025, 7, 25, 10, 0))
                .endDate(LocalDateTime.of(2025, 7, 25, 11, 0))
                .durationMinutes(60)
                .createdAt(LocalDateTime.now())
                .build();

        given(detailRepo.findById(1)).willReturn(Optional.of(detail));

        // When
        MentoringMatchDetailResponse res = sut.findById(1);

        // Then
        assertThat(res.getTopic()).isEqualTo("Java 기초");
        assertThat(res.getSummary()).isEqualTo("OOP 개념 정리");
    }

    @DisplayName("존재하지 않는 ID로 조회 시 예외 발생")
    @Test
    void givenInvalidId_whenFindingDetail_thenThrowsException() {
        // Given
        given(detailRepo.findById(999)).willReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> sut.findById(999))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining("매칭 상세 정보를 찾을 수 없습니다.");
    }

    @DisplayName("update 요청이 오면 매칭 상세 정보를 수정한다")
    @Test
    void givenUpdateRequest_whenUpdatingDetail_thenModifiesAndReturnsResponse() {
        MentoringMatch match = MentoringMatch.builder().matchId(1).build();
        // Given
        MentoringMatchDetail existing = MentoringMatchDetail.builder()
                .id(1)
                .mentoringMatch(match)
                .topic("이전 주제")
                .summary("이전 요약")
                .sessionDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(1))
                .durationMinutes(60)
                .build();

        given(detailRepo.findById(1)).willReturn(Optional.of(existing));

        MentoringMatchDetailUpdateRequest req = updateRequest();

        // When
        MentoringMatchDetailResponse res = sut.update(1, req);

        // Then
        assertThat(res.getTopic()).isEqualTo("Spring 심화");
        assertThat(res.getSummary()).isEqualTo("DI, AOP 정리");
        then(detailRepo).should().findById(1);
    }

    @DisplayName("존재하지 않는 ID로 수정 요청 시 예외 발생")
    @Test
    void givenInvalidId_whenUpdating_thenThrowsException() {
        // Given
        given(detailRepo.findById(123)).willReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> sut.update(123, updateRequest()))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining("매칭 상세 정보를 찾을 수 없습니다.");
    }
}

package com.example.campy.service.mentoring;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.MentoringTag;
import com.example.campy.entity.User;
import com.example.campy.repository.MentoringOfferRepository;
import com.example.campy.repository.MentoringTagPostRepository;
import com.example.campy.repository.MentoringTagRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.service.MentoringOfferService;
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
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("멘토링 제안(offer) 로직")
@ExtendWith(MockitoExtension.class)
class MentoringOfferServiceTest {

    @InjectMocks private MentoringOfferService sut;

    @Mock private UserRepository userRepository;
    @Mock private MentoringOfferRepository offerRepo;
    @Mock private MentoringTagRepository tagRepo;
    @Mock private MentoringTagPostRepository tagPostRepo;

    private final Pageable defaultPageable = PageRequest.of(0, 10);

    User dummyUser = User.builder()
            .userId(99)
            .nickname("테스트유저")
            .profileImg("/images/default-mentor.png")
            .build();

    private MentoringOffer createOffer(int offerId, String title) {
        User dummyUser = User.builder()
                .userId(99)
                .nickname("테스트유저")
                .profileImg("/images/default-mentor.png")
                .build();

        return MentoringOffer.builder()
                .offerId(offerId)
                .user(dummyUser)
                .title(title)
                .description("설명")
                .status(MentoringStatus.ONGOING)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void mockTagNames(int offerId, List<String> tags) {
        given(tagPostRepo.findTagNamesByOfferId(offerId)).willReturn(tags);
    }

    private void assertOfferResponse(MentoringOfferResponse res, String expectedTitle, List<String> expectedTags, MentoringStatus expectedStatus) {
        assertThat(res).isNotNull();
        assertThat(res.getTitle()).isEqualTo(expectedTitle);
        assertThat(res.getTags()).containsExactlyInAnyOrderElementsOf(expectedTags);
        assertThat(res.getStatus()).isEqualTo(expectedStatus.getLabel());
    }

    @DisplayName("유효한 요청과 유저ID가 주어지면 멘토링 제안을 저장하고 Response를 반환한다.")
    @Test
    void givenOfferRequest_whenCreating_thenCreatesOfferAndReturnResponse() {
        Integer userId = 1;
        MentoringOfferCreateRequest req = MentoringOfferCreateRequest.builder()
                .title("Java 멘토링")
                .description("테스트 설명")
                .tags(List.of("spring", "jpa"))
                .build();

        User user = User.builder().userId(userId).build();
        MentoringOffer offerEntity = req.toEntity(user);
        MentoringOffer savedOffer = offerEntity.toBuilder().offerId(123).build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(offerRepo.save(any(MentoringOffer.class))).willReturn(savedOffer);
        given(tagRepo.findByName(anyString())).willReturn(Optional.empty());
        given(tagRepo.save(any(MentoringTag.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(tagPostRepo.saveAll(anyList())).willAnswer(invocation -> invocation.getArgument(0));

        MentoringOfferResponse result = sut.create(req, userId);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Java 멘토링");
    }

    @DisplayName("offerId로 조회하면 해당 멘토링 제안과 태그를 반환한다.")
    @Test
    void givenOfferId_whenFinding_thenReturnsOfferResponse() {
        Integer offerId = 123;
        MentoringOffer offer = createOffer(offerId, "테스트");
        List<String> tags = List.of("java", "spring");

        given(offerRepo.findById(offerId)).willReturn(Optional.of(offer));
        mockTagNames(offerId, tags);

        MentoringOfferResponse result = sut.findById(offerId);

        assertOfferResponse(result, "테스트", tags, MentoringStatus.ONGOING);
    }

    @DisplayName("offerId가 유효하면 소프트 삭제를 수행한다.")
    @Test
    void givenOfferId_whenDeleting_thenSetsIsDeletedTrue() {
        Integer offerId = 123;
        MentoringOffer offer = createOffer(offerId, "삭제 대상");

        given(offerRepo.findById(offerId)).willReturn(Optional.of(offer));

        sut.delete(offerId);

        assertThat(offer.getIsDeleted()).isTrue();
    }

    @DisplayName("userId로 조회하면 해당 사용자의 제안을 반환한다.")
    @Test
    void givenUserId_whenFinding_thenReturnsUserOffers() {
        Integer userId = 1;
        MentoringOffer offer1 = createOffer(1, "A");
        MentoringOffer offer2 = createOffer(2, "B");
        given(offerRepo.findByUser_UserIdAndIsDeletedFalse(userId)).willReturn(List.of(offer1, offer2));
        given(tagPostRepo.findTagNamesByOfferId(anyInt())).willReturn(List.of("tag"));

        List<MentoringOfferResponse> result = sut.findByUserId(userId);

        assertThat(result).hasSize(2);
    }

    @DisplayName("페이지 정보가 주어지면 전체 멘토링 제안을 반환한다.")
    @Test
    void givenPageable_whenFindingAll_thenReturnsPageOfOffers() {
        MentoringOffer offer1 = createOffer(1, "제안1");
        MentoringOffer offer2 = createOffer(2, "제안2");

        given(offerRepo.findByIsDeletedFalse(defaultPageable)).willReturn(new PageImpl<>(List.of(offer1, offer2)));
        given(tagPostRepo.findTagNamesGroupedByOfferIds(List.of(1, 2))).willReturn(Map.of(
                1, List.of("java", "spring"),
                2, List.of("db", "jpa")
        ));

        Page<MentoringOfferResponse> result = sut.findAll(defaultPageable);

        assertThat(result.getContent()).hasSize(2);
    }

    @DisplayName("상태값이 주어지면 해당 상태의 멘토링 제안들을 반환한다.")
    @Test
    void givenStatus_whenFindingByStatus_thenReturnsFilteredOffers() {
        MentoringOffer offer1 = createOffer(1, "진행중 제안 1");
        MentoringOffer offer2 = createOffer(2, "진행중 제안 2");

        given(offerRepo.findByStatusAndIsDeletedFalse(MentoringStatus.ONGOING, defaultPageable)).willReturn(new PageImpl<>(List.of(offer1, offer2)));
        mockTagNames(1, List.of("java"));
        mockTagNames(2, List.of("spring"));

        Page<MentoringOfferResponse> result = sut.findByStatus(MentoringStatus.ONGOING, defaultPageable);

        assertThat(result).hasSize(2);
    }

    @DisplayName("키워드가 주어지면 해당 키워드가 포함된 멘토링 제안을 반환한다.")
    @Test
    void givenKeyword_whenSearchingOffers_thenReturnsMatchingOffers() {
        String keyword = "Spring";
        MentoringOffer offer = createOffer(1, "Spring Boot 입문");

        given(offerRepo.searchWithKeyword(keyword, defaultPageable)).willReturn(new PageImpl<>(List.of(offer)));
        mockTagNames(1, List.of("spring", "boot"));

        Page<MentoringOfferResponse> result = sut.searchOffers(keyword, defaultPageable);

        assertOfferResponse(result.getContent().get(0), "Spring Boot 입문", List.of("spring", "boot"), MentoringStatus.ONGOING);
    }

    @DisplayName("유효한 요청이 주어지면 멘토링 제안을 수정하고 응답을 반환한다.")
    @Test
    void givenValidUpdateRequest_whenUpdatingOffer_thenUpdatesAndReturnsResponse() {
        // Given
        Integer offerId = 1;
        MentoringOffer existingOffer = createOffer(offerId, "수정 전 제목");

        MentoringOfferUpdateRequest req = MentoringOfferUpdateRequest.builder()
                .title("수정된 제목")
                .description("수정된 설명")
                .price(10000)
                .location("온라인")
                .maxParticipants(3)
                .duration(60)
                .status(MentoringStatus.ONGOING)
                .tags(List.of("java", "spring"))
                .build();

        given(offerRepo.findById(offerId)).willReturn(Optional.of(existingOffer));
        given(tagRepo.findByName(anyString())).willReturn(Optional.empty());
        given(tagRepo.save(any(MentoringTag.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(tagPostRepo.saveAll(anyList()))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When
        MentoringOfferResponse result = sut.update(offerId, req);

        // Then
        assertOfferResponse(result, "수정된 제목", List.of("java", "spring"), MentoringStatus.ONGOING);

        then(offerRepo).should().findById(offerId);
        then(tagPostRepo).should().deleteByMentoringOffer(existingOffer);
        then(tagRepo).should(times(2)).findByName(anyString());
        then(tagRepo).should(times(2)).save(any(MentoringTag.class));
        then(tagPostRepo).should().saveAll(anyList());
    }

    @DisplayName("태그 키워드가 주어지면 해당 태그가 포함된 멘토링 제안을 반환한다.")
    @Test
    void givenTagKeyword_whenSearchingByTag_thenReturnsMatchingOffers() {
        // Given
        String tagKeyword = "spring";
        MentoringOffer offer = createOffer(1, "Spring 멘토링");

        given(offerRepo.searchByTag(tagKeyword, defaultPageable))
                .willReturn(new PageImpl<>(List.of(offer)));
        mockTagNames(1, List.of("spring", "boot"));

        // When
        Page<MentoringOfferResponse> result = sut.searchByTag(tagKeyword, defaultPageable);

        // Then
        assertThat(result).hasSize(1);
        assertOfferResponse(result.getContent().get(0), "Spring 멘토링", List.of("spring", "boot"), MentoringStatus.ONGOING);

        then(offerRepo).should().searchByTag(tagKeyword, defaultPageable);
        then(tagPostRepo).should().findTagNamesByOfferId(1);
    }
}

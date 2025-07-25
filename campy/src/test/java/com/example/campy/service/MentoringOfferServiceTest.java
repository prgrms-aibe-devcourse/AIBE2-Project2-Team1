package com.example.campy.service;

import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.MentoringTag;
import com.example.campy.entity.User;
import com.example.campy.repository.MentoringOfferRepository;
import com.example.campy.repository.MentoringTagPostRepository;
import com.example.campy.repository.MentoringTagRepository;
import com.example.campy.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("멘토링 제안(offer) 로직")
@ExtendWith(MockitoExtension.class)
class MentoringOfferServiceTest  {

    @InjectMocks private MentoringOfferService sut;

    @Mock private  UserRepository userRepository;
    @Mock private MentoringOfferRepository offerRepo;
    @Mock private MentoringTagRepository tagRepo;
    @Mock private MentoringTagPostRepository tagPostRepo;


    @DisplayName("유효한 요청과 유저ID가 주어지면 멘토링 제안을 저장하고 Response를 반환한다.")
    @Test
    void givenOfferRequest_whenCreating_thenCreatesOfferAndReturnResponse() {
        // Given
        Integer userId = 1;
        MentoringOfferCreateRequest req = MentoringOfferCreateRequest.builder()
                .title("Java 멘토링")
                .description("테스트 설명")
                .tags(List.of("spring", "jpa"))
                .build();

        User user = User.builder().userId(userId).build();

        // 실제 로직에서 사용하는 객체 그대로 재활용
        MentoringOffer offerEntity = req.toEntity(user);

        MentoringOffer savedOffer = offerEntity.toBuilder()
                .offerId(123) // 저장된 후의 ID만 수동 설정
                .build();

        // mocking
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(offerRepo.save(any(MentoringOffer.class))).willReturn(savedOffer);
        given(tagRepo.findByName(anyString())).willReturn(Optional.empty());
        given(tagRepo.save(any(MentoringTag.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(tagPostRepo.saveAll(anyList()))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When
        MentoringOfferResponse result = sut.create(req, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Java 멘토링");

        then(userRepository).should().findById(userId);
        then(offerRepo).should().save(any(MentoringOffer.class));
        then(tagRepo).should(times(2)).findByName(anyString());
        then(tagRepo).should(times(2)).save(any(MentoringTag.class));
        then(tagPostRepo).should().saveAll(anyList());
    }
}
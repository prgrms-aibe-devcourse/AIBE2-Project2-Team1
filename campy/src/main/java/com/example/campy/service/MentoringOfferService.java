package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
import com.example.campy.constant.MentoringStatus;
import com.example.campy.dto.mentoring.request.MentoringOfferCreateRequest;
import com.example.campy.dto.mentoring.request.MentoringOfferUpdateRequest;
import com.example.campy.dto.mentoring.response.MentoringOfferResponse;
import com.example.campy.entity.MentoringOffer;
import com.example.campy.entity.MentoringTag;
import com.example.campy.entity.MentoringTagPost;
import com.example.campy.exception.GeneralException;
import com.example.campy.repository.MentoringOfferRepository;
import com.example.campy.repository.UserRepository;
import com.example.campy.entity.User;
import com.example.campy.repository.MentoringTagPostRepository;
import com.example.campy.repository.MentoringTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MentoringOfferService {


    private final UserRepository userRepository;
    private final MentoringOfferRepository offerRepo;
    private final MentoringTagRepository tagRepo;
    private final MentoringTagPostRepository tagPostRepo;

    public MentoringOfferResponse create(MentoringOfferCreateRequest req, Integer userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        MentoringOffer entity = req.toEntity(user); // User 객체 전달
        MentoringOffer saved = offerRepo.save(entity);

        // 해시 태그 처리 로직
        List<MentoringTagPost> tagPosts = req.getTags().stream()
                .map(tagName -> {
                    MentoringTag tag = tagRepo.findByName(tagName)
                            .orElseGet(() -> tagRepo.save(
                                    MentoringTag.builder().name(tagName).build()
                            ));

                    return MentoringTagPost.builder()
                            .mentoringOffer(entity)
                            .tag(tag)
                            .build();
                })
                .toList();

        tagPostRepo.saveAll(tagPosts);

        return MentoringOfferResponse.from(saved); // Entity -> Response DTO
    }

    // 전체 멘토링 제안 리스트
    public Page<MentoringOfferResponse> findAll(Pageable pageable){
        Page<MentoringOffer> offerPage = offerRepo.findByIsDeletedFalse(pageable);

        List<Integer> offerIds = offerPage.getContent().stream()
                .map(MentoringOffer::getOfferId)
                .toList();

        Map<Integer, List<String>> tagMap = tagPostRepo.findTagNamesGroupedByOfferIds(offerIds);

        List<MentoringOfferResponse> content = offerPage.getContent().stream()
                .map(offer -> {
                    List<String> tags = tagMap.getOrDefault(offer.getOfferId(), List.of());
                    return MentoringOfferResponse.from(offer, tags);
                })
                .toList();

        return new PageImpl<>(content, pageable, offerPage.getTotalElements());
    }

    public MentoringOfferResponse findById(Integer offerId){
        MentoringOffer offer = offerRepo.findById(offerId)
                .filter(o -> !o.getIsDeleted())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND, "해당 제안이 존재하지 않거나 삭제되었습니다."));

        List<String> tagNames = tagPostRepo.findTagNamesByOfferId(offerId);

        return MentoringOfferResponse.from(offer, tagNames);
    }

    public List<MentoringOfferResponse> findByUserId(Integer userId){

        return offerRepo.findByUser_UserIdAndIsDeletedFalse(userId)
                .stream()
                .map(offer -> {
                    List<String> tagNames = tagPostRepo.findTagNamesByOfferId(offer.getOfferId());
                    return MentoringOfferResponse.from(offer, tagNames);
                })
                .toList();
    }

    @Transactional
    public MentoringOfferResponse update(Integer offerId, MentoringOfferUpdateRequest req){
        MentoringOffer offer = offerRepo.findById(offerId)
                .filter(o -> !o.getIsDeleted())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        offer.setTitle(req.getTitle());
        offer.setDescription(req.getDescription());
        offer.setPrice(req.getPrice());
        offer.setLocation(req.getLocation());
        offer.setMaxParticipants(req.getMaxParticipants());
        offer.setDuration(req.getDuration());
        offer.setStatus(req.getStatus());
        offer.setUpdatedAt(LocalDateTime.now());

        // 기존 태그 삭제
        tagPostRepo.deleteByMentoringOffer(offer);

        // 새로 태그 저장
        List<MentoringTagPost> newTagPosts = req.getTags().stream()
                .map(tagName -> {
                    MentoringTag tag = tagRepo.findByName(tagName)
                            .orElseGet(() -> tagRepo.save(MentoringTag.builder().name(tagName).build()));

                    return MentoringTagPost.builder()
                            .mentoringOffer(offer)
                            .tag(tag)
                            .build();
                })
                .toList();

        tagPostRepo.saveAll(newTagPosts);

        return MentoringOfferResponse.from(offer, req.getTags());
    }

    @Transactional
    public void delete(Integer offerId){
        MentoringOffer offer = offerRepo.findById(offerId)
                .filter(o -> !o.getIsDeleted())
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND));

        offer.setIsDeleted(true);
        offer.setUpdatedAt(LocalDateTime.now());

    }

    // 키워드 검색
    public Page<MentoringOfferResponse> searchOffers(String keyword, Pageable pageable){
        Page<MentoringOffer> offers = offerRepo.searchWithKeyword(keyword, pageable);

        return offers.map(offer -> {
            List<String> tagNames = tagPostRepo.findTagNamesByOfferId(offer.getOfferId());
            return MentoringOfferResponse.from(offer, tagNames);
        });
    }

    // 해시태그 검색
    public Page<MentoringOfferResponse> searchByTag(String tagKeyword, Pageable pageable){
        Page<MentoringOffer> offers = offerRepo.searchByTag(tagKeyword, pageable);

        return offers.map(offer -> {
            List<String> tagNames = tagPostRepo.findTagNamesByOfferId(offer.getOfferId());
            return MentoringOfferResponse.from(offer, tagNames);
        });
    }

    // 상태별 조회
    public Page<MentoringOfferResponse> findByStatus(MentoringStatus status, Pageable pageable){
        Page<MentoringOffer> offers = offerRepo.findByStatusAndIsDeletedFalse(status, pageable);

        return offers.map(offer -> {
            List<String> tagNames = tagPostRepo.findTagNamesByOfferId(offer.getOfferId());
            return MentoringOfferResponse.from(offer, tagNames);
        });
    }

}

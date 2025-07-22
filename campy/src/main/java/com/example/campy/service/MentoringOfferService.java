package com.example.campy.service;

import com.example.campy.constant.ErrorCode;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<MentoringOfferResponse> findAll(){
        return offerRepo.findByIsDeletedFalse()
                .stream()
                .map(offer -> {
                    List<String> tagNames = tagPostRepo.findTagNamesByOfferId(offer.getOfferId());

                    return MentoringOfferResponse.from(offer, tagNames);
                })
                .toList();
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

}

package com.example.campy.service;

import com.example.campy.dto.talent.request.TalentCreateRequest;
import com.example.campy.dto.talent.request.TalentUpdateRequest;
import com.example.campy.dto.talent.response.TalentResponseDto;
import com.example.campy.entity.Talent;
import com.example.campy.entity.User;
import com.example.campy.repository.TalentRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TalentService {

    private final TalentRepository talentRepository;
    private final UserRepository userRepository;

    public List<TalentResponseDto> getAllTalents() {
        return talentRepository.findAll().stream()
                .map(TalentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public TalentResponseDto getTalentById(Integer talentId) {
        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new IllegalArgumentException("Talent not found with id: " + talentId));
        return TalentResponseDto.fromEntity(talent);
    }

    @Transactional
    public TalentResponseDto createTalent(TalentCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.userId()));

        Talent talent = Talent.builder()
                .user(user)
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .availableDays(request.availableDays())
                .offlineLocation(request.offlineLocation())
                .status(request.status() != null ? request.status() : "요청중")
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .imagePath(request.imagePath())
                .category(request.category())
                .build();
        return TalentResponseDto.fromEntity(talentRepository.save(talent));
    }

    @Transactional
    public TalentResponseDto updateTalent(Integer talentId, TalentUpdateRequest request) {
        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new IllegalArgumentException("Talent not found with id: " + talentId));

        talent.setTitle(request.title());
        talent.setDescription(request.description());
        talent.setPrice(request.price());
        talent.setAvailableDays(request.availableDays());
        talent.setOfflineLocation(request.offlineLocation());
        talent.setStatus(request.status());
        talent.setIsDeleted(request.isDeleted());
        talent.setImagePath(request.imagePath());
        talent.setCategory(request.category());
        talent.setUpdatedAt(LocalDateTime.now());

        return TalentResponseDto.fromEntity(talentRepository.save(talent));
    }

    @Transactional
    public void deleteTalent(Integer talentId) {
        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new IllegalArgumentException("Talent not found with id: " + talentId));
        talent.setIsDeleted(true);
        talent.setUpdatedAt(LocalDateTime.now());
        talentRepository.save(talent);
    }
}

package com.example.campy.service;

import com.example.campy.dto.talent.request.TalentCreateRequest;
import com.example.campy.dto.talent.response.TalentResponseDto;
import com.example.campy.dto.talent.request.TalentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TalentService {
    TalentResponseDto createTalent(TalentCreateRequest request, MultipartFile image, Authentication authentication) throws IOException;
    TalentResponseDto getTalentById(Integer talentId);
    Page<TalentResponseDto> getAllTalents(Pageable pageable, String status, String category, String tag);
    TalentResponseDto updateTalent(Integer id, TalentUpdateRequest request, MultipartFile image, Authentication authentication) throws IOException;
    void deleteTalent(Integer talentId, Authentication authentication);

    // Admin 전용 메소드
    TalentResponseDto adminUpdateTalent(Integer id, TalentUpdateRequest request, MultipartFile image) throws IOException;
    void adminDeleteTalent(Integer talentId);

    List<TalentResponseDto> getAllTalentsByUser(Authentication authentication);
}
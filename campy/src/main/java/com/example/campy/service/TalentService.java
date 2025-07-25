package com.example.campy.service;

import com.example.campy.dto.talent.TalentCreateRequest;
import com.example.campy.dto.talent.TalentResponseDto;
import com.example.campy.dto.talent.TalentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TalentService {
    TalentResponseDto createTalent(TalentCreateRequest request, MultipartFile image) throws IOException;
    TalentResponseDto getTalentById(Integer talentId);
    Page<TalentResponseDto> getAllTalents(Pageable pageable, String status, String category, String tag);
    TalentResponseDto updateTalent(Integer id, TalentUpdateRequest request, MultipartFile image) throws IOException;
    void deleteTalent(Integer talentId, Integer userId);
}
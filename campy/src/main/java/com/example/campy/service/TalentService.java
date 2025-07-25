package com.example.campy.service;

import com.example.campy.entity.Talent;
import java.util.List;
import org.springframework.data.domain.Page;

public interface TalentService {
    Talent createTalent(Talent talent);
    Talent getTalentById(Integer talentId);
    List<Talent> getAllTalents();
    Page<Talent> getAllTalents(org.springframework.data.domain.Pageable pageable, String status, String category, String tag);
    Talent updateTalent(Integer talentId, Talent updatedTalent);
    Talent updateTalent(Integer id, com.example.campy.dto.TalentRequestDto request, org.springframework.web.multipart.MultipartFile image) throws java.io.IOException;
    void deleteTalent(Integer talentId);
    void deleteTalent(Integer id, Integer userId);
    Talent registerTalent(com.example.campy.dto.TalentRequestDto request, org.springframework.web.multipart.MultipartFile image) throws java.io.IOException;
}

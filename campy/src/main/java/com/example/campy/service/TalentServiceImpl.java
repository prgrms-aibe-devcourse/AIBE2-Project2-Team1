package com.example.campy.service;

import com.example.campy.entity.Talent;
import com.example.campy.repository.TalentRepository;
import com.example.campy.repository.TagRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;
import com.example.campy.entity.Tag;
import com.example.campy.entity.User;
import com.example.campy.dto.TalentRequestDto;


@Service
@RequiredArgsConstructor
public class TalentServiceImpl implements TalentService {

    private final TalentRepository talentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public Talent createTalent(Talent talent) {
        return talentRepository.save(talent);
    }

    @Override
    public Talent getTalentById(Integer talentId) {
        return talentRepository.findById(talentId)
                .orElseThrow(() -> new RuntimeException("Talent not found with id " + talentId));
    }

    @Override
    public List<Talent> getAllTalents() {
        return talentRepository.findAll();
    }

    @Override
    public Page<Talent> getAllTalents(org.springframework.data.domain.Pageable pageable, String status, String category, String tag) {
        if (tag != null && category != null) {
            return talentRepository.findByTags_NameAndCategoryAndIsDeletedFalse(tag, category, pageable);
        } else if (tag != null) {
            return talentRepository.findByTags_NameAndIsDeletedFalse(tag, pageable);
        } else if (status != null && category != null) {
            return talentRepository.findByIsDeletedFalseAndStatusAndCategory(status, category, pageable);
        } else if (status != null) {
            return talentRepository.findByIsDeletedFalseAndStatus(status, pageable);
        } else if (category != null) {
            return talentRepository.findByIsDeletedFalseAndCategory(category, pageable);
        } else {
            return talentRepository.findByIsDeletedFalse(pageable);
        }
    }

    @Override
    public Talent updateTalent(Integer talentId, Talent updatedTalent) {
        Talent existingTalent = talentRepository.findById(talentId)
                .orElseThrow(() -> new RuntimeException("Talent not found with id " + talentId));

        // Update fields
        existingTalent.setTitle(updatedTalent.getTitle());
        existingTalent.setDescription(updatedTalent.getDescription());
        existingTalent.setPrice(updatedTalent.getPrice());
        existingTalent.setAvailableDays(updatedTalent.getAvailableDays());
        existingTalent.setOfflineLocation(updatedTalent.getOfflineLocation());
        existingTalent.setStatus(updatedTalent.getStatus());
        existingTalent.setIsDeleted(updatedTalent.getIsDeleted());
        existingTalent.setImagePath(updatedTalent.getImagePath());
        existingTalent.setCategory(updatedTalent.getCategory());
        // Note: User and Tags relationships might need more complex handling depending on requirements

        return talentRepository.save(existingTalent);
    }

    @Override
    public void deleteTalent(Integer talentId) {
        talentRepository.deleteById(talentId);
    }

    @Override
    public void deleteTalent(Integer id, Integer userId) {
        talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getIsDeleted()))
                .map(talent -> {
                    if (!talent.getUser().getUserId().equals(userId)) {
                        throw new RuntimeException("Unauthorized to delete this talent");
                    }
                    talent.setIsDeleted(true);
                    talent.setUpdatedAt(LocalDateTime.now());
                    talentRepository.save(talent);
                    return talent;
                })
                .orElseThrow(() -> new RuntimeException("Talent not found with id " + id));
    }

    @Override
    public Talent registerTalent(TalentRequestDto request, MultipartFile image) throws IOException {
        Integer userId = 1; // JWT 사용 시 교체
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
            File dest = new File(uploadPath, fileName);
            image.transferTo(dest);
            imagePath = dest.getAbsolutePath();
        }

        List<Tag> tagEntities = new ArrayList<>();
        for (String tagName : request.getTagNames()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
            tagEntities.add(tag);
        }

        Talent talent = Talent.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .availableDays(request.getAvailableDays())
                .offlineLocation(request.getOfflineLocation())
                .status("요청중")
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .imagePath(imagePath)
                .category(request.getCategory())
                .tags(new HashSet<>(tagEntities))
                .build();

        return talentRepository.save(talent);
    }

    @Override
    public Talent updateTalent(Integer id, TalentRequestDto request, MultipartFile image) throws IOException {
        Integer userId = 1; // JWT 사용 시 교체

        return talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getIsDeleted()))
                .map(talent -> {
                    if (!talent.getUser().getUserId().equals(userId)) {
                        throw new RuntimeException("Unauthorized to update this talent");
                    }
                    talent.setTitle(request.getTitle());
                    talent.setDescription(request.getDescription());
                    talent.setPrice(request.getPrice());
                    talent.setAvailableDays(request.getAvailableDays());
                    talent.setOfflineLocation(request.getOfflineLocation());
                    talent.setCategory(request.getCategory());

                    // 이미지 처리
                    String imagePath = talent.getImagePath();
                    if (image != null && !image.isEmpty()) {
                        String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
                        File dest = new File(uploadPath, fileName);
                        try {
                            image.transferTo(dest);
                            imagePath = dest.getAbsolutePath();
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to upload image", e);
                        }
                    }
                    talent.setImagePath(imagePath);

                    // 태그 처리
                    List<Tag> tagEntities = new ArrayList<>();
                    if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
                        for (String tagName : request.getTagNames()) {
                            Tag tag = tagRepository.findByName(tagName)
                                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
                            tagEntities.add(tag);
                        }
                    }
                    talent.setTags(new HashSet<>(tagEntities));

                    talent.setUpdatedAt(LocalDateTime.now());
                    return talentRepository.save(talent);
                })
                .orElseThrow(() -> new RuntimeException("Talent not found with id " + id));
    }
}

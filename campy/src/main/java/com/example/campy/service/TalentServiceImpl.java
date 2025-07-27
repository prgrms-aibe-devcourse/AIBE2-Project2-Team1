package com.example.campy.service;

import com.example.campy.dto.talent.request.TalentCreateRequest;
import com.example.campy.dto.talent.response.TalentResponseDto;
import com.example.campy.dto.talent.request.TalentUpdateRequest;
import com.example.campy.dto.user.response.UserResponseDto;
import com.example.campy.entity.Tag;
import com.example.campy.entity.Talent;
import com.example.campy.entity.User;
import com.example.campy.repository.TagRepository;
import com.example.campy.repository.TalentRepository;
import com.example.campy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import jakarta.annotation.PostConstruct; // ← 추가
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TalentServiceImpl implements TalentService {

    private final TalentRepository talentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    // 서버 시작 시 폴더 자동 생성
    @PostConstruct
    public void initUploadDir() {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    public TalentResponseDto createTalent(TalentCreateRequest request, MultipartFile image, Authentication authentication) throws IOException {
        User user;
        if (request.getUserId() != null) {
            // 관리자가 userId를 지정한 경우
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        } else {
            // 일반 사용자가 자신의 재능을 생성하는 경우 (Authentication 사용)
            String username = authentication.getName();
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        }

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
            File dest = new File(uploadPath, fileName);
            image.transferTo(dest);
            imagePath = fileName; // DB에는 파일명만 저장!
        }

        List<Tag> tagEntities = new ArrayList<>();
        if (request.getTagNames() != null) {
            for (String tagName : request.getTagNames()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
                tagEntities.add(tag);
            }
        }

        Talent talent = Talent.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .availableDays(request.getAvailableDays())
                .offlineLocation(request.getOfflineLocation())
                .status("요청중")
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .imagePath(imagePath)  // 파일명만 저장!
                .category(request.getCategory())
                .tags(new HashSet<>(tagEntities))
                .build();

        Talent savedTalent = talentRepository.save(talent);
        return toResponseDto(savedTalent);
    }

    @Override
    public TalentResponseDto getTalentById(Integer talentId) {
        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new RuntimeException("Talent not found with id " + talentId));
        return toResponseDto(talent);
    }

    @Override
    public Page<TalentResponseDto> getAllTalents(Pageable pageable, String status, String category, String tag) {
        Page<Talent> talentsPage;
        if (tag != null && category != null) {
            talentsPage = talentRepository.findByTags_NameAndCategoryAndDeletedFalse(tag, category, pageable);
        } else if (tag != null) {
            talentsPage = talentRepository.findByTags_NameAndDeletedFalse(tag, pageable);
        } else if (status != null && category != null) {
            talentsPage = talentRepository.findByDeletedFalseAndStatusAndCategory(status, category, pageable);
        } else if (status != null) {
            talentsPage = talentRepository.findByDeletedFalseAndStatus(status, pageable);
        } else if (category != null) {
            talentsPage = talentRepository.findByDeletedFalseAndCategory(category, pageable);
        } else {
            talentsPage = talentRepository.findByDeletedFalse(pageable);
        }
        return talentsPage.map(this::toResponseDto);
    }

    @Override
    public TalentResponseDto updateTalent(Integer id, TalentUpdateRequest request, MultipartFile image, Authentication authentication) throws IOException {
        String username = authentication.getName(); // 현재 로그인한 사용자의 username 가져오기
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Talent existingTalent = talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getDeleted()))
                .orElseThrow(() -> new RuntimeException("Talent not found with id " + id));

        if (!existingTalent.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Unauthorized to update this talent");
        }

        existingTalent.setTitle(request.getTitle());
        existingTalent.setDescription(request.getDescription());
        existingTalent.setPrice(request.getPrice());
        existingTalent.setAvailableDays(request.getAvailableDays());
        existingTalent.setOfflineLocation(request.getOfflineLocation());
        existingTalent.setCategory(request.getCategory());

        // 이미지 처리
        String imagePath = existingTalent.getImagePath();
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
            File dest = new File(uploadPath, fileName);
            image.transferTo(dest);
            imagePath = fileName; // 파일명만 저장
        }
        existingTalent.setImagePath(imagePath);

        // 태그 처리
        List<Tag> tagEntities = new ArrayList<>();
        if (request.getTagNames() != null) {
            for (String tagName : request.getTagNames()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
                tagEntities.add(tag);
            }
        }
        existingTalent.setTags(new HashSet<>(tagEntities));

        existingTalent.setUpdatedAt(LocalDateTime.now());
        Talent updatedTalent = talentRepository.save(existingTalent);
        return toResponseDto(updatedTalent);
    }

    @Override
    public void deleteTalent(Integer talentId, Authentication authentication) {
        String username = authentication.getName(); // 현재 로그인한 사용자의 username 가져오기
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new RuntimeException("Talent not found with id " + talentId));

        if (!talent.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("Unauthorized to delete this talent");
        }

        talent.setDeleted(true);
        talent.setUpdatedAt(LocalDateTime.now());
        talentRepository.save(talent);
    }

    @Override
    public TalentResponseDto adminUpdateTalent(Integer id, TalentUpdateRequest request, MultipartFile image) throws IOException {
        Talent existingTalent = talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getDeleted()))
                .orElseThrow(() -> new RuntimeException("Talent not found with id " + id));

        existingTalent.setTitle(request.getTitle());
        existingTalent.setDescription(request.getDescription());
        existingTalent.setPrice(request.getPrice());
        existingTalent.setAvailableDays(request.getAvailableDays());
        existingTalent.setOfflineLocation(request.getOfflineLocation());
        existingTalent.setCategory(request.getCategory());

        // 이미지 처리
        String imagePath = existingTalent.getImagePath();
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(image.getOriginalFilename());
            File dest = new File(uploadPath, fileName);
            image.transferTo(dest);
            imagePath = fileName; // 파일명만 저장
        }
        existingTalent.setImagePath(imagePath);

        // 태그 처리
        List<Tag> tagEntities = new ArrayList<>();
        if (request.getTagNames() != null) {
            for (String tagName : request.getTagNames()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
                tagEntities.add(tag);
            }
        }
        existingTalent.setTags(new HashSet<>(tagEntities));

        existingTalent.setUpdatedAt(LocalDateTime.now());
        Talent updatedTalent = talentRepository.save(existingTalent);
        return toResponseDto(updatedTalent);
    }

    @Override
    public void adminDeleteTalent(Integer talentId) {
        Talent talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new RuntimeException("Talent not found with id " + talentId));

        talent.setDeleted(true);
        talent.setUpdatedAt(LocalDateTime.now());
        talentRepository.save(talent);
    }

    private TalentResponseDto toResponseDto(Talent talent) {
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .userId(talent.getUser().getUserId())
                .username(talent.getUser().getUsername())
                .email(talent.getUser().getEmail())
                .name(talent.getUser().getName())
                .nickname(talent.getUser().getNickname())
                .major(talent.getUser().getMajor())
                .school(talent.getUser().getSchool())
                .entranceYear(talent.getUser().getEntranceYear())
                .role(talent.getUser().getRole())
                .isVerified(talent.getUser().getIsVerified())
                .profileImg(talent.getUser().getProfileImg())
                .intro(talent.getUser().getIntro())
                .build();

        List<String> tagNames = talent.getTags().stream()
                .map(Tag::getName)
                .toList();

        return TalentResponseDto.builder()
                .talentId(talent.getTalentId())
                .title(talent.getTitle())
                .description(talent.getDescription())
                .price(talent.getPrice())
                .availableDays(talent.getAvailableDays())
                .offlineLocation(talent.getOfflineLocation())
                .status(talent.getStatus())
                .imagePath(talent.getImagePath()) // 반드시 파일명만 넘어가야 함
                .category(talent.getCategory())
                .createdAt(talent.getCreatedAt())
                .updatedAt(talent.getUpdatedAt())
                .user(userResponseDto)
                .tagNames(tagNames)
                .build();
    }
}
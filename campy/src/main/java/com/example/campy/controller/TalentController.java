package com.example.campy.controller;

import com.example.campy.dto.TalentRequestDto;
import com.example.campy.entity.Tag;
import com.example.campy.entity.Talent;
import com.example.campy.entity.User;
import com.example.campy.jwt.JwtUtil;
import com.example.campy.repository.TagRepository;
import com.example.campy.repository.TalentRepository;
import com.example.campy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/talents")
public class TalentController {

    private final TalentRepository talentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${upload.path}")
    private String uploadPath;

    private User extractUserFromRequest(HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("토큰 누락");
        }
        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("유효하지 않은 토큰");
        }
        Integer userId = jwtUtil.getUserId(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
    }

    @GetMapping
    public ResponseEntity<Page<Talent>> getAllTalents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Talent> talents;

        if (tag != null && category != null) {
            talents = talentRepository.findByTags_NameAndCategoryAndIsDeletedFalse(tag, category, pageable);
        } else if (tag != null) {
            talents = talentRepository.findByTags_NameAndIsDeletedFalse(tag, pageable);
        } else if (status != null && category != null) {
            talents = talentRepository.findByIsDeletedFalseAndStatusAndCategory(status, category, pageable);
        } else if (status != null) {
            talents = talentRepository.findByIsDeletedFalseAndStatus(status, pageable);
        } else if (category != null) {
            talents = talentRepository.findByIsDeletedFalseAndCategory(category, pageable);
        } else {
            talents = talentRepository.findByIsDeletedFalse(pageable);
        }

        return ResponseEntity.ok(talents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Talent> getTalentById(@PathVariable Integer id) {
        return talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getIsDeleted()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Talent>> searchByTag(
            @RequestParam String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Talent> talents = talentRepository.findByTags_NameAndIsDeletedFalse(tag, pageable);
        return ResponseEntity.ok(talents);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Talent> registerTalent(
            @RequestPart("data") TalentRequestDto request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest servletRequest) throws IOException {

        User user;
        try {
            user = extractUserFromRequest(servletRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }

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

        return ResponseEntity.ok(talentRepository.save(talent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Talent> updateTalent(@PathVariable Integer id, @RequestBody TalentRequestDto request, HttpServletRequest servletRequest) {
        User user;
        try {
            user = extractUserFromRequest(servletRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }

        return talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getIsDeleted()))
                .map(talent -> {
                    if (!talent.getUser().getUserId().equals(user.getUserId())) {
                        return ResponseEntity.status(403).<Talent>build();
                    }
                    talent.setTitle(request.getTitle());
                    talent.setDescription(request.getDescription());
                    talent.setPrice(request.getPrice());
                    talent.setAvailableDays(request.getAvailableDays());
                    talent.setOfflineLocation(request.getOfflineLocation());
                    talent.setUpdatedAt(LocalDateTime.now());
                    return ResponseEntity.ok(talentRepository.save(talent));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTalent(@PathVariable Integer id, HttpServletRequest servletRequest) {
        User user;
        try {
            user = extractUserFromRequest(servletRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }

        return talentRepository.findById(id)
                .filter(talent -> !Boolean.TRUE.equals(talent.getIsDeleted()))
                .map(talent -> {
                    if (!talent.getUser().getUserId().equals(user.getUserId())) {
                        return ResponseEntity.status(403).<Void>build();
                    }
                    talent.setIsDeleted(true);
                    talent.setUpdatedAt(LocalDateTime.now());
                    talentRepository.save(talent);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
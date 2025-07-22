package com.example.campy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TalentRequestDto {
    private String title;
    private String description;
    private Integer price;
    private String availableDays;
    private String offlineLocation;
    private MultipartFile image;
    private String category;
    private List<String> tagNames;
}
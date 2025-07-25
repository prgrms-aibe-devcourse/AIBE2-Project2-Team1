package com.example.campy.dto.talent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalentCreateRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;

    @NotNull(message = "가격은 필수입니다.")
    private Integer price;

    private String availableDays;
    private String offlineLocation;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    private List<String> tagNames;
}

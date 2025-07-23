package com.example.campy.dto.user.request;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String nickname;

    @Size(max = 100)
    private String major;

    @Size(max = 100)
    private String school;

    private Integer entranceYear;

    @NotBlank
    private String role;

    private Boolean isVerified;

    @Size(max = 255)
    private String profileImg;

    private String intro;
}

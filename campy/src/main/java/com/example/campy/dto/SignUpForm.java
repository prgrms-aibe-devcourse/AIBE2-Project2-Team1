package com.example.campy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpForm {
    private String username;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String school;
    private String major;
    private Integer entranceYear;
}

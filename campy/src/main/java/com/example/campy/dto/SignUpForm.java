package com.example.campy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpForm {
    private String email;
    private String password;
    private String name;
    private String nickname;
}

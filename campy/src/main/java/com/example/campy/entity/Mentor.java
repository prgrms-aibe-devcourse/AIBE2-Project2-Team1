package com.example.campy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mentor {
    private Long id;
    private String name;
    private String expertise;
    private String imagePath;
}

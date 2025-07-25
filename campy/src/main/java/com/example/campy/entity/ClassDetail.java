package com.example.campy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassDetail {
    private Long id;
    private String title;
    private String category;
    private int price;
    private String method;
    private Long mentorId;
}
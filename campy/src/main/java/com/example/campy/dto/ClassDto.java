package com.example.campy.dto;

import java.util.List;

public class ClassDto {
    private String title;
    private String description;
    private String mentorName;
    private String thumbnail;
    private List<String> tags;

    public ClassDto(String title, String description, String mentorName, String thumbnail, List<String> tags) {
        this.title = title;
        this.description = description;
        this.mentorName = mentorName;
        this.thumbnail = thumbnail;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

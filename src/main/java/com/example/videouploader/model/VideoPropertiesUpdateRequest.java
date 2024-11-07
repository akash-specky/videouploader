package com.example.videouploader.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoPropertiesUpdateRequest {
    private Integer frameHeight;
    private Integer frameWidth;
    private Double frameRate;
    private LocalDateTime modifiedDate;
}

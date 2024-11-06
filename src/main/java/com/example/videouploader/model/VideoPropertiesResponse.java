package com.example.videouploader.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPropertiesResponse {

    Integer id;
    private Integer frameWidth;
    private Integer frameHeight;
    private Double frameRate;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}

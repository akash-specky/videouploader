package com.example.videouploader.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class VideoProperties {

    @Id
    Integer id;

    private Integer frameWidth;
    private Integer frameHeight;
    private Double frameRate;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public VideoProperties(long size, String codec, String format, float fps) {
    }
}

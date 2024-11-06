package com.example.videouploader.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetailsResponse {


    Integer id;

    String path;

    String fileName;
    long duration;
    Long size;
    String codec;
    String format;
    double fps;

    Map<Integer, String> videoResolutions = new HashMap<>();

    Map<String, String> videoThumbnails =  new HashMap<>();

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    VideoPropertiesResponse videoPropertiesResponse;
}

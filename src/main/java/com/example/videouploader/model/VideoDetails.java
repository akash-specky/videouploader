package com.example.videouploader.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetails {

    @Id
    Integer id;

    String path;

    String fileName;
    long duration;
    Long size;
    String codec;
    String format;
    double fps;
    boolean isResolutiosAvailable;

    Map<Integer, String> videoResolutions = new HashMap<>();

    Map<String, String> videoThumbnails = new HashMap<>();

    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedBy
    LocalDateTime updatedAt;

    VideoProperties videoProperties;

    boolean published;
    boolean visible;

    Long viewCount;


    public VideoDetails(Integer id, String path, String fileName, long duration, Long size, String codec, String format, double fps, boolean isResolutiosAvailable, LocalDateTime createdAt, LocalDateTime updatedAt, VideoProperties videoProperties) {
        this.id = id;
        this.path = path;
        this.fileName = fileName;
        this.duration = duration;
        this.size = size;
        this.codec = codec;
        this.format = format;
        this.fps = fps;
        this.isResolutiosAvailable = isResolutiosAvailable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.videoProperties = videoProperties;
    }
}

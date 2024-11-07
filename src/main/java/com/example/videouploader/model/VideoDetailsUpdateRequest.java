package com.example.videouploader.model;

import lombok.Data;

import java.util.Map;

@Data
public class VideoDetailsUpdateRequest {
    private String path;
    private String fileName;
    private long duration;
    private Long size;
    private String codec;
    private String format;
    private double fps;
    private Map<Integer, String> videoResolutions;
    private Map<String, String> videoThumbnails;
    private VideoPropertiesUpdateRequest videoProperties;
}

package com.example.videouploader.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "videos")
public class Video {

    @Id
    private String videoId;
    private String title;
    private String format;       // e.g., "mp4", "avi"
    private String duration;      // e.g., "short", "10min"
    private Date uploadTime;

    // Constructors
    public Video() {}

    public Video(String videoId, String title, String format, String duration, Date uploadTime) {
        this.videoId = videoId;
        this.title = title;
        this.format = format;
        this.duration = duration;
        this.uploadTime = uploadTime;
    }

    // Getters and Setters
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}

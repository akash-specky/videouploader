package com.example.videouploader.model;

public class VideoStatusResponse {

    private String videoId;
    private String status;

    public VideoStatusResponse(String videoId, String status) {
        this.videoId = videoId;
        this.status = status;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

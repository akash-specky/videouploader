package com.example.videouploader.model;

public class VideoUploadResponse {

    private String videoId;
    private String message;

    public VideoUploadResponse() {
    }

    public VideoUploadResponse(String videoId, String message) {
        this.videoId = videoId;
        this.message = message;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.example.videouploader.controller;


import com.example.videouploader.model.VideoUploadResponse;
import com.example.videouploader.service.VideoProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/videos")
public class VideoController {


    @Autowired
    private VideoProcessingService videoProcessingService;


    @PostMapping("/upload")
    public ResponseEntity<VideoUploadResponse> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            String videoId = videoProcessingService.processUploadedVideo(file);
            return ResponseEntity.ok(new VideoUploadResponse(videoId, "Video uploaded successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VideoUploadResponse(null, "Failed to upload video"));
        }
    }
}

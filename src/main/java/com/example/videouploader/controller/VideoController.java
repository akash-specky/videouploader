package com.example.videouploader.controller;


import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.model.VideoProperties;
import com.example.videouploader.model.VideoUploadResponse;
import com.example.videouploader.service.VideoProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.InvalidPropertiesFormatException;
import java.util.List;

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


    @GetMapping("/getVideo/{id}")
    public ResponseEntity<VideoDetails> getVideo(@PathVariable Integer id) throws VideoException {

        return new ResponseEntity<>(videoProcessingService.getVideoById(id), HttpStatus.OK);

    }

    @GetMapping("/getAllVideos")
    public ResponseEntity<List<VideoDetails>> getAllVideos() throws VideoException {

        return new ResponseEntity<>(videoProcessingService.getAllVideos(), HttpStatus.OK);

    }

    @PostMapping("/getPagination")
    public ResponseEntity<PaginatedResponse> getAllVideosWithPagination(@RequestBody PaginationDTO paginationDTO) throws VideoException {

        return new ResponseEntity<>(videoProcessingService.getAllVideosWithPagination(paginationDTO), HttpStatus.OK);

    }
}

package com.example.videouploader.controller;


import com.example.videouploader.Exception.CustomVideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.model.VideoUploadResponse;
import com.example.videouploader.service.VideoProcessingService;
import com.example.videouploader.serviceImpl.GoogleTokenValidator;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {
    @Autowired
    private GoogleTokenValidator googleTokenValidator;

    @Autowired
    private VideoProcessingService videoProcessingService;


    @PostMapping("/upload")
    public ResponseEntity<VideoUploadResponse> uploadVideo(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "resolution", defaultValue = "720p") String resolution) {

        String token = authorization.split(" ")[1];

        try {

            GoogleIdToken.Payload payload = googleTokenValidator.validateToken(token);

            if (payload != null) {
                String videoId = videoProcessingService.processUploadedVideo(file, resolution);
                return ResponseEntity.ok(new VideoUploadResponse(videoId, "Video uploaded successfully!"));

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( new VideoUploadResponse(null,"Invalid token"));

            }


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new VideoUploadResponse(null, "Failed to upload video"));
        }
    }


    @GetMapping("/getVideo/{id}")
    public ResponseEntity<VideoDetails> getVideo(@PathVariable Integer id) throws CustomVideoException {

        return new ResponseEntity<>(videoProcessingService.getVideoById(id), HttpStatus.OK);

    }

    @GetMapping("/getAllVideos")
    public ResponseEntity<List<VideoDetails>> getAllVideos() throws CustomVideoException {

        return new ResponseEntity<>(videoProcessingService.getAllVideos(), HttpStatus.OK);

    }

    @PostMapping("/getPagination")
    public ResponseEntity<PaginatedResponse> getAllVideosWithPagination(@RequestBody PaginationDTO paginationDTO) throws CustomVideoException {

        return new ResponseEntity<>(videoProcessingService.getAllVideosWithPagination(paginationDTO), HttpStatus.OK);

    }


    @PostMapping("/uploadThumbnail")
    public ResponseEntity<String> uploadThumbnail(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(videoProcessingService.uploadThumbnail(file), HttpStatus.OK);
    }

}

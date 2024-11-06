package com.example.videouploader.controller;


import com.example.videouploader.Exception.CustomVideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.dtos.CommonResponseDTO;
import com.example.videouploader.dtos.SearchDTO;
import com.example.videouploader.exceptions.InvalidInputException;
import com.example.videouploader.model.*;
import com.example.videouploader.service.VideoProcessingService;
import com.example.videouploader.service.VideoService;
import com.example.videouploader.serviceImpl.GoogleTokenValidator;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {
    @Autowired
    private GoogleTokenValidator googleTokenValidator;

    @Autowired
    private VideoProcessingService videoProcessingService;

    @Autowired
    private VideoService videoService;


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
    public ResponseEntity<CommonResponseDTO> getVideo(@PathVariable Integer id) {
        try {
            VideoDetailsResponse videoDetailsResponse = videoProcessingService.getVideoById(id);
            return new ResponseEntity<>(new CommonResponseDTO(true, "Successfull!", videoDetailsResponse), HttpStatus.OK);
        } catch (CustomVideoException e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, e.getMessage(), new VideoDetailsResponse()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getAllVideos")
    public ResponseEntity< List<VideoDetailsResponse>> getAllVideos() throws CustomVideoException {

        return new ResponseEntity<>(videoProcessingService.getAllVideos(), HttpStatus.OK);

    }

    @PostMapping("/getPagination")
    public ResponseEntity<PaginatedResponse> getAllVideosWithPagination(@RequestBody PaginationDTO paginationDTO) throws CustomVideoException {

        return new ResponseEntity<>(videoProcessingService.getAllVideosWithPagination(paginationDTO), HttpStatus.OK);

    }

    @PostMapping("/search")
    public ResponseEntity<CommonResponseDTO> searchVideos(@RequestBody SearchDTO searchDTO) {


            List<Video> videos = videoService.searchVideos(searchDTO);
            return ResponseEntity.ok(new CommonResponseDTO(true,"",videos));


    }
}

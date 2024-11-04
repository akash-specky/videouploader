package com.example.videouploader.controller;


import com.example.videouploader.Exception.CustomVideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.dtos.CommonResponseDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetailsResponse;
import com.example.videouploader.model.VideoUploadResponse;
import com.example.videouploader.service.VideoProcessingService;
import com.example.videouploader.serviceImpl.GoogleTokenValidator;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.taskdefs.rmic.XNewRmic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Slf4j
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
    public ResponseEntity<CommonResponseDTO> getVideo(@PathVariable Integer id) {
        try {
            VideoDetailsResponse videoDetailsResponse = videoProcessingService.getVideoById(id);
            return new ResponseEntity<>(new CommonResponseDTO(true, "Successfull!", videoDetailsResponse), HttpStatus.OK);
        } catch (CustomVideoException e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, e.getMessage(), new VideoDetailsResponse()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getAllVideos")
    public ResponseEntity<CommonResponseDTO> getAllVideos() {
        try {
            List<VideoDetailsResponse> videoDetailsResponse = videoProcessingService.getAllVideos();
            return new ResponseEntity<>(new CommonResponseDTO(true, "Successfull!", videoDetailsResponse), HttpStatus.OK);
        } catch (CustomVideoException e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, e.getMessage(), new VideoDetailsResponse()), HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @PostMapping("/getAllVideosByPagination")
    public ResponseEntity<CommonResponseDTO> getAllVideosWithPagination(@Valid @RequestBody PaginationDTO paginationDTO) {
        try {
            PaginatedResponse paginatedResponse = videoProcessingService.getAllVideosWithPagination(paginationDTO);
            return new ResponseEntity<>(new CommonResponseDTO(true, "Successfull!", paginatedResponse), HttpStatus.OK);
        } catch (CustomVideoException e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, e.getMessage(), new PaginatedResponse()), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/uploadThumbnail/{id}")
    public ResponseEntity<CommonResponseDTO> uploadThumbnail(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
        try {
            String string = videoProcessingService.uploadThumbnail(file, id);
            return new ResponseEntity<>(new CommonResponseDTO(true, "Successfull!", string), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonResponseDTO(false, e.getMessage(), new Object()), HttpStatus.BAD_REQUEST);
        }
    }

}

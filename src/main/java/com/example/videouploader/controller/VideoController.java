package com.example.videouploader.controller;


import com.example.videouploader.Exception.CustomVideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.dtos.CommonResponseDTO;
import com.example.videouploader.dtos.SearchDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.Video;
import com.example.videouploader.model.VideoDetailsResponse;
import com.example.videouploader.model.VideoUploadResponse;
import com.example.videouploader.service.VideoProcessingService;
import com.example.videouploader.service.VideoService;
import com.example.videouploader.serviceImpl.GoogleTokenValidator;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.extern.slf4j.Slf4j;
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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new VideoUploadResponse(null, "Invalid token"));

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
    @PutMapping("/{id}/publish")
    public ResponseEntity<String> publishVideo(@PathVariable Long id, @RequestParam boolean publish) {
        videoProcessingService.updatePublishStatus(id, publish);
        return ResponseEntity.ok(publish ? "Video published successfully." : "Video unpublished successfully.");
    }


    @PutMapping("/{id}/visibility")
    public ResponseEntity<String> setVideoVisibility(@PathVariable Long id, @RequestParam boolean visible) {
        videoProcessingService.updateVisibility(id, visible);
        return ResponseEntity.ok(visible ? "Video is now visible to the public." : "Video is now hidden from the public.");
    }

    @PostMapping("/{videoId}/view")
    public ResponseEntity<String> registerView(
            @PathVariable Long videoId,
            @RequestParam String ipAddress,
            @RequestParam String deviceId) {

        videoProcessingService.updateViewCount(videoId, ipAddress, deviceId);
        return ResponseEntity.ok("View registered successfully");
    }

}

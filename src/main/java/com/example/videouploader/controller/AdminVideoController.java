package com.example.videouploader.controller;

import com.example.videouploader.Exception.CustomVideoException;
import com.example.videouploader.model.AdminVideoCountResponse;
import com.example.videouploader.model.AdminVideoDeleteResponse;
import com.example.videouploader.model.VideoDetailsResponse;
import com.example.videouploader.model.VideoDetailsUpdateRequest;
import com.example.videouploader.service.AdminVideoService;
import com.example.videouploader.service.VideoProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adminVideo")
public class AdminVideoController {

    @Autowired
    private AdminVideoService adminVideoService;

    @Autowired
    private VideoProcessingService videoProcessingService;


    @GetMapping("/getVideo/{id}")
    public ResponseEntity<VideoDetailsResponse> getVideoById(@PathVariable Integer id) throws CustomVideoException {
        return new ResponseEntity<>(videoProcessingService.getVideoById(id), HttpStatus.OK);
    }

    @GetMapping("/getAllVideos")
    public ResponseEntity<List<VideoDetailsResponse>> getAllVideos() throws CustomVideoException {
        return new ResponseEntity<>(videoProcessingService.getAllVideos(), HttpStatus.OK);
    }


    @PutMapping("/updateVideo/{id}")
    public ResponseEntity<VideoDetailsResponse> updateVideo(
            @PathVariable Integer id,
            @RequestBody VideoDetailsUpdateRequest updateRequest) throws CustomVideoException {
        VideoDetailsResponse updatedVideo = videoProcessingService.updateVideo(id, updateRequest);
        return new ResponseEntity<>(updatedVideo, HttpStatus.OK);
    }

    @DeleteMapping("/deleteVideo/{id}")
    public ResponseEntity<AdminVideoDeleteResponse> deleteVideo(@PathVariable Long id) {
        try {
            adminVideoService.deleteVideo(id);
            return ResponseEntity.ok(new AdminVideoDeleteResponse(id, "Video deleted successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AdminVideoDeleteResponse(id, "Failed to delete video with id: " + id));
        }
    }

    @GetMapping("/videos/count")
    public ResponseEntity<AdminVideoCountResponse> countVideos() {
        try {
            long videoCount = adminVideoService.countVideos();
            return ResponseEntity.ok(new AdminVideoCountResponse(videoCount, "Video count retrieved successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AdminVideoCountResponse(0, "Failed to retrieve video count"));
        }
    }
}


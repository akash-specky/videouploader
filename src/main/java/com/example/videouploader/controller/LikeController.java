package com.example.videouploader.controller;

import com.example.videouploader.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/{videoId}/like")
    public ResponseEntity<String> likeVideo(@RequestParam String userId, @PathVariable String videoId) {
        String message = likeService.likeVideo(userId, videoId);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/{videoId}/likes")
    public ResponseEntity<Long> getLikesCount(@PathVariable String videoId) {
        long count = likeService.getLikesCount(videoId);
        return ResponseEntity.ok(count);
    }

}

package com.example.videouploader.controller;


import com.example.videouploader.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/videos")
public class ShareController {

    @Autowired
    private ShareService shareService;

    @PostMapping("/{videoId}/share")
    public ResponseEntity<String> generateTinyUrl(@PathVariable String videoId) {
        String tinyUrl = shareService.generateTinyUrl(videoId);
        return ResponseEntity.ok(tinyUrl);
    }
}

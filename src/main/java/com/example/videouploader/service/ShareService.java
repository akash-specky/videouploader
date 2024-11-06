package com.example.videouploader.service;

import org.springframework.stereotype.Service;

@Service
public interface ShareService {

    public String generateTinyUrl(String videoId);
    public String getVideoId(String videoId);
}

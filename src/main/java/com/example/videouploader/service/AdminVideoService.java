package com.example.videouploader.service;

import com.example.videouploader.model.VideoDetails;
import org.springframework.stereotype.Service;

@Service
public interface AdminVideoService {

    VideoDetails updateVideo(Long id, VideoDetails videoDetails);

    void deleteVideo(Long id);

    long countVideos();
}


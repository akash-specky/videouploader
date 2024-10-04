package com.example.videouploader.service;


import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.model.VideoProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface VideoProcessingService {

    public String processUploadedVideo(MultipartFile file) throws Exception;

    public String saveVideoProperties(String videoPath, VideoProperties properties) throws VideoException;

    VideoDetails getVideoById(Integer id) throws VideoException;
}

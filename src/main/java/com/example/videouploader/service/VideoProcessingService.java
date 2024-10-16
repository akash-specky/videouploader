package com.example.videouploader.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface VideoProcessingService {

    public String processUploadedVideo(MultipartFile file,String resolution) throws Exception;
}

package com.example.videouploader.service;


import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface VideoChunkingService {

    public List<File> chunkVideo(File videoFile, String videoId) throws Exception;
}

package com.example.videouploader.service;


import com.example.videouploader.exceptions.InvalidInputException;
import com.example.videouploader.model.Video;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VideoService {


    public List<Video> searchVideos(String duration, String format, String uploadTime, String query) throws InvalidInputException;
}

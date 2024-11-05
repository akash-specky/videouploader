package com.example.videouploader.service;


import org.springframework.stereotype.Service;

@Service
public interface LikeService {

    public String likeVideo(String userId, String videoId);
    public long getLikesCount(String videoId);
}

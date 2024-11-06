package com.example.videouploader.service;


import com.example.videouploader.dto.LikeDTO;
import org.springframework.stereotype.Service;

@Service
public interface LikeService {

    public String likeVideo(LikeDTO likeDTO);
    public long getLikesCount(String videoId);
}

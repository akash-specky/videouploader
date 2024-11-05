package com.example.videouploader.serviceImpl;

import com.example.videouploader.model.Like;
import com.example.videouploader.repository.LikeRepository;
import com.example.videouploader.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;




    @Override
    public String likeVideo(String userId, String videoId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndVideoId(userId, videoId);
        if (existingLike.isEmpty()) {
            Like like = new Like();
            like.setUserId(userId);
            like.setVideoId(videoId);
            likeRepository.save(like);
            return "Liked successfully!";
        } else {
            return "User has already liked this video!";
        }
    }

    @Override
    public long getLikesCount(String videoId) {
        return likeRepository.countByVideoId(videoId);
    }
}

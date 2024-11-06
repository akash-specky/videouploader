package com.example.videouploader.serviceImpl;

import com.example.videouploader.dto.LikeDTO;
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
    public String likeVideo(LikeDTO likeDTO) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndVideoId(likeDTO.getUserId(), likeDTO.getVideoId());
        if (existingLike.isEmpty()) {
            Like like = new Like();
            like.setUserId(likeDTO.getUserId());
            like.setVideoId(likeDTO.getVideoId());
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

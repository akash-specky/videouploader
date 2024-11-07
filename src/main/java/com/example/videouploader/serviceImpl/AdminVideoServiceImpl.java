package com.example.videouploader.serviceImpl;

import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.repository.VideoDetailsRepository;
import com.example.videouploader.service.AdminVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminVideoServiceImpl implements AdminVideoService {

    @Autowired
    private VideoDetailsRepository videoDetailsRepository;


    @Override
    public VideoDetails updateVideo(Long id, VideoDetails videoDetails) {
        VideoDetails video = videoDetailsRepository.findById(id);
        return videoDetailsRepository.save(video);
    }

    @Override
    public void deleteVideo(Long id) {
        VideoDetails video = videoDetailsRepository.findById(id);
        videoDetailsRepository.delete(video);
    }

    @Override
    public long countVideos() {
        return videoDetailsRepository.count();
    }
}


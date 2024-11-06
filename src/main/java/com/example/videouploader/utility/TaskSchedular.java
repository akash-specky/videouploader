package com.example.videouploader.utility;


import com.example.videouploader.service.VideoProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class TaskSchedular {


    @Autowired
    VideoProcessingService videoProcessingService;


//    @Scheduled(fixedRate = 60000)
     public void videorResolutionsConvertion() {

        try {
            videoProcessingService.processUnconvertedVideos();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

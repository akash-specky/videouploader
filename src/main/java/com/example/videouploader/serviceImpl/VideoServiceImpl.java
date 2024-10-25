package com.example.videouploader.serviceImpl;

import com.example.videouploader.dtos.FilterParams;
import com.example.videouploader.exceptions.InvalidInputException;
import com.example.videouploader.model.Video;
import com.example.videouploader.repository.VideoRepository;
import com.example.videouploader.service.NLPService;
import com.example.videouploader.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private NLPService nlpService;

    @Autowired
    private VideoRepository videoRepository;


    @Override
    public List<Video> searchVideos(String duration, String format, String uploadTime, String query) throws InvalidInputException {
        FilterParams filterParams = nlpService.processQuery(query);

        Integer durationFrom = filterParams.getDurationFrom();
        Integer durationTo = filterParams.getDurationTo();
        String finalFormat = format != null ? format : filterParams.getFormat();
        Date uploadTimeFrom = filterParams.getUploadTimeFrom();
        Date uploadTimeTo = filterParams.getUploadTimeTo();

        return videoRepository.findVideosByFilter(durationFrom, durationTo, finalFormat, uploadTimeFrom, uploadTimeTo);
    }
}

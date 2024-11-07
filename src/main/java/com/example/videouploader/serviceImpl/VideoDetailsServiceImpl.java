package com.example.videouploader.serviceImpl;

import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.repository.VideoDetailsRepository;
import com.example.videouploader.service.VideoDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoDetailsServiceImpl implements VideoDetailsService {

    @Autowired
    private VideoDetailsRepository videoDetailsRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<String> getDistinctDuration() {
        return mongoTemplate.query(VideoDetails.class).distinct("duration").as(Long.class).all().stream().map(String::valueOf).collect(Collectors.toList());
    }

    private List<String> getDistinctFormats() {
        return mongoTemplate.query(VideoDetails.class).distinct("duration").as(Integer.class).all().stream().map(String::valueOf).collect(Collectors.toList());
    }

    private List<String> getDistinctFrameHeights() {
        return mongoTemplate.query(VideoDetails.class).distinct("videoProperties.frameHeight").as(Integer.class).all().stream().map(String::valueOf).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<String>> getFilterOptions() {
        Map<String, List<String>> filterOptions = new HashMap<>();
        filterOptions.put("videoDurations", getDistinctDuration());
        filterOptions.put("videoQualities", getDistinctFrameHeights());
        filterOptions.put("videoFormats", getDistinctFormats());
        List<String> videoCreatedTimes = videoDetailsRepository.findAll().stream().filter(video -> video != null && video.getCreatedAt() != null).map(video -> {
            LocalDateTime createdDate = video.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime currentDate = LocalDateTime.now();
            long minutesDifference = Duration.between(createdDate, currentDate).toMinutes();
            return String.valueOf(minutesDifference);
        }).collect(Collectors.toList());
        filterOptions.put("videoCreatedTimes", videoCreatedTimes);
        return filterOptions;
    }
}


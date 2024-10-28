package com.example.videouploader.serviceImpl;

import com.example.videouploader.dtos.SearchDTO;
import com.example.videouploader.exceptions.InvalidInputException;
import com.example.videouploader.model.Video;
import com.example.videouploader.repository.VideoRepository;
import com.example.videouploader.service.NLPService;
import com.example.videouploader.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private NLPService nlpService;

    @Autowired
    private VideoRepository videoRepository;


    private static final String DEFAULT_START_DATE = "1970-01-01";
    private static final String DEFAULT_END_DATE = "2099-12-31";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");



    @Override
    public List<Video> searchVideos(SearchDTO searchDTO) throws InvalidInputException {

        SearchDTO processedSearch = nlpService.processQuery(searchDTO.getQuery());

        String duration = searchDTO.getDuration() != null ? searchDTO.getDuration() : processedSearch.getDuration();
        String format = searchDTO.getFormat() != null ? searchDTO.getFormat() : processedSearch.getFormat();
        String uploadTime = searchDTO.getUploadTime() != null ? searchDTO.getUploadTime() : processedSearch.getUploadTime();

        String durationFrom = parseDurationFrom(duration);
        String durationTo = parseDurationTo(duration);
        Date uploadTimeFrom = parseUploadTimeFrom(uploadTime);
        Date uploadTimeTo = parseUploadTimeTo(uploadTime);


        return videoRepository.findVideosByFilter(durationFrom, durationTo, format, uploadTimeFrom, uploadTimeTo);
    }
    private String parseDurationFrom(String duration) {
        if (duration == null || duration.isEmpty()) {
            return "0";
        }

        String[] parts = duration.split("-");
        return parts.length > 0 ? parts[0].trim() : duration.trim();
    }

    private String parseDurationTo(String duration) {
        if (duration == null || duration.isEmpty()) {
            return "9999";
        }

        String[] parts = duration.split("-");
        return parts.length > 1 ? parts[1].trim() : duration.trim();
    }
    private Date parseUploadTimeFrom(String uploadTime) {
        if (uploadTime == null || uploadTime.isEmpty()) {
            return parseDate(DEFAULT_START_DATE);
        }

        String[] parts = uploadTime.split("to");
        return parseDate(parts[0].trim());
    }
    private Date parseDate(String dateStr) {
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr);
        }
    }


    private Date parseUploadTimeTo(String uploadTime) {
        if (uploadTime == null || uploadTime.isEmpty()) {
            return parseDate(DEFAULT_END_DATE);
        }

        String[] parts = uploadTime.split("to");
        return parts.length > 1 ? parseDate(parts[1].trim()) : parseDate(parts[0].trim());
    }

}

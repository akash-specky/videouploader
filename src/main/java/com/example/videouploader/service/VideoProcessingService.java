package com.example.videouploader.service;


import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.dtos.PaginationDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface VideoProcessingService {

    public String processUploadedVideo(MultipartFile file) throws Exception;

//    public String saveVideoProperties(String videoPath, VideoProperties properties) throws VideoException, InvalidPropertiesFormatException;

    VideoDetails getVideoById(Integer id) throws VideoException;

    List<VideoDetails> getAllVideos() throws VideoException;

    PaginatedResponse getAllVideosWithPagination(PaginationDTO paginationDTO) throws VideoException;
    public String processUploadedVideo(MultipartFile file,String resolution) throws Exception;
}

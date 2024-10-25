package com.example.videouploader.service;


import com.example.videouploader.Exception.CustomVideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.model.VideoProperties;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Service
public interface VideoProcessingService {

    public String processUploadedVideo(MultipartFile file) throws Exception;

    VideoDetails getVideoById(Integer id) throws CustomVideoException;

    List<VideoDetails> getAllVideos() throws CustomVideoException;

    PaginatedResponse getAllVideosWithPagination(PaginationDTO paginationDTO) throws CustomVideoException;
    public String processUploadedVideo(MultipartFile file,String resolution) throws Exception;

    String uploadThumbnail(MultipartFile multipartFile);
}

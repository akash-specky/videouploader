package com.example.videouploader.service;


import com.example.videouploader.Exception.CustomVideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetailsResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface VideoProcessingService {



    VideoDetailsResponse getVideoById(Integer id) throws CustomVideoException;

    public String processUploadedVideo(MultipartFile file,String resolution) throws Exception;
    List<VideoDetailsResponse> getAllVideos() throws CustomVideoException;

    PaginatedResponse getAllVideosWithPagination(PaginationDTO paginationDTO) throws CustomVideoException;

    String uploadThumbnail(MultipartFile multipartFile, Integer VideoId);

    public String processUnconvertedVideos() throws IOException;

}

package com.example.videouploader.service;


import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.model.VideoProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Service
public interface VideoProcessingService {

    public String processUploadedVideo(MultipartFile file) throws Exception;

//    public String saveVideoProperties(String videoPath, VideoProperties properties) throws VideoException, InvalidPropertiesFormatException;

    VideoDetails getVideoById(Integer id) throws VideoException;

    List<VideoDetails> getAllVideos() throws VideoException;

    PaginatedResponse getAllVideosWithPagination(PaginationDTO paginationDTO) throws VideoException;

    String uploadThumbnail(MultipartFile multipartFile);

    String convertVideosAsync() throws IOException;
}

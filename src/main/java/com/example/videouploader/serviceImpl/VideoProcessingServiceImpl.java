package com.example.videouploader.serviceImpl;

import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.model.VideoProperties;
import com.example.videouploader.repository.VideoDetailsRepository;
import com.example.videouploader.repository.VideoPropertiesRepository;
import com.example.videouploader.service.VideoChunkingService;
import com.example.videouploader.service.VideoCombiningService;
import com.example.videouploader.service.VideoProcessingService;
import com.example.videouploader.utility.CustomSequences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static com.example.videouploader.utility.Constant.UPLOAD_DIR;


@Service
public class VideoProcessingServiceImpl implements VideoProcessingService {


    @Autowired
    CustomSequences sequences;

    @Autowired
    VideoPropertiesRepository videoPropertiesRepository;


    @Autowired
    VideoDetailsRepository videoDetailsRepository;

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private VideoChunkingService videoChunkingService;

    @Autowired
    private VideoCombiningService videoCombiningService;

    @Override
    public String processUploadedVideo(MultipartFile file) throws Exception {
//        File tempFile = saveFileToDirectory(file);
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        System.out.println(file.getInputStream());
        System.out.println(file);

        return " ";
//
//        String videoId = UUID.randomUUID().toString();
//        List<File> chunks = videoChunkingService.chunkVideo(tempFile, videoId);
//        processChunksAsync(chunks, videoId);
//
//
//        return videoId;
    }

    @Override
    public String saveVideoProperties(String videoPath, VideoProperties properties) throws VideoException {

//        File file = new File(videoPath);

//        if (!file.exists()){
//            throw new IllegalArgumentException("Invalid file path");
//        }
//        if (!properties.getFormat().equalsIgnoreCase("mp4")){
//            throw new IllegalArgumentException("Invalid Video format exception");
//        }

        try{
            VideoDetails videoDetails = new VideoDetails();
            videoDetails.setId(sequences.getNextSequence("videoDetails"));
            videoDetails.setPath(videoPath);
            videoDetails.setVideoProperties(properties);
            videoDetailsRepository.save(videoDetails);

            return "Properties saved successfully!";

        }catch (Exception e){
            throw new VideoException("Invalid video details!");
        }

    }

    @Override
    public VideoDetails getVideoById(Integer id) throws VideoException {

            Optional<VideoDetails> optional = videoDetailsRepository.findById(id);
            if (optional.isPresent()) {
                return videoDetailsRepository.findById(id).get();
            }
            throw new VideoException("Invalid video details!");

    }

    @Override
    public List<VideoDetails> getAllVideos() throws VideoException {

        List<VideoDetails> videoDetailsList = videoDetailsRepository.findAll();

        if (videoDetailsList.isEmpty()){
            throw new VideoException("No Videos is availible");
        }
        return videoDetailsList;
    }

    @Override
    public PaginatedResponse getAllVideosWithPagination(Integer page, Integer size) throws VideoException {

        Pageable pageable = PageRequest.of(page,size);
        Page<VideoDetails> videoPage = videoDetailsRepository.findAll(pageable);

//        if (videoPage.isEmpty()){
//            throw new VideoException("No Videos is availible");
//        }

        return new PaginatedResponse(
                videoPage.getContent(),
                videoPage.getTotalElements(),
                videoPage.getTotalPages(),
                page,
                size
        );
    }

    private void processChunksAsync(List<File> chunks, String videoId) {
        for (File chunk : chunks) {
            taskExecutor.execute(() -> {
                processChunk(chunk, videoId);
            });
        }

        taskExecutor.execute(() -> {
            try {
                File combinedVideo = videoCombiningService.combineChunks(chunks, videoId);
                System.out.println("Combined video saved at: " + combinedVideo.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void processChunk(File chunk, String videoId) {
        try {

            System.out.println("Processing chunk: " + chunk.getName()+"  "+videoId);
        } catch (Exception e) {
            System.err.println("Error processing chunk: " + chunk.getName());
            e.printStackTrace();
        }
    }

    private File saveFileToDirectory(MultipartFile file) throws IOException {


        File savedFile = new File(UPLOAD_DIR, Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(savedFile);

        return savedFile;
    }



    }

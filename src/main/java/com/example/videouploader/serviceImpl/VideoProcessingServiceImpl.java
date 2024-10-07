package com.example.videouploader.serviceImpl;

import com.example.videouploader.service.VideoChunkingService;
import com.example.videouploader.service.VideoCombiningService;
import com.example.videouploader.service.VideoProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static com.example.videouploader.utility.Constant.UPLOAD_DIR;


@Service
public class VideoProcessingServiceImpl implements VideoProcessingService {




    @Autowired
    private Executor taskExecutor;

    @Autowired
    private VideoChunkingService videoChunkingService;

    @Autowired
    private VideoCombiningService videoCombiningService;

    @Override
    public String processUploadedVideo(MultipartFile file) throws Exception {
        File tempFile = saveFileToDirectory(file);
        String videoId = UUID.randomUUID().toString();
        List<File> chunks = videoChunkingService.chunkVideo(tempFile, videoId);
        processChunksAsync(chunks, videoId);


        return videoId;
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

            System.err.println("Processing chunk: " + chunk.getName()+"  "+videoId);
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

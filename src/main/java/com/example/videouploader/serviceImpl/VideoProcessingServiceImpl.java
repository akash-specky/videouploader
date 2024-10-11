package com.example.videouploader.serviceImpl;

import com.example.videouploader.service.VideoChunkingService;
import com.example.videouploader.service.VideoCombiningService;
import com.example.videouploader.service.VideoProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.videouploader.utility.Constant.*;

@Service
public class VideoProcessingServiceImpl implements VideoProcessingService {

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private VideoChunkingService videoChunkingService;

    @Autowired
    private VideoCombiningService videoCombiningService;

    private static final Logger logger = LoggerFactory.getLogger(VideoProcessingServiceImpl.class);

    @Override
    public String processUploadedVideo(MultipartFile file, String resolution) throws Exception {
        File tempFile = saveFileToDirectory(file);
        String videoId = UUID.randomUUID().toString();
        List<File> chunks = videoChunkingService.chunkVideo(tempFile, videoId, resolution);

        processChunksAsync(chunks, videoId, file.getOriginalFilename(), tempFile);

        return videoId;
    }

    @Async
    public void processChunksAsync(List<File> chunks, String videoId, String fileName, File tempFile) {
        AtomicInteger processedChunks = new AtomicInteger(0);

        CompletableFuture[] futures = chunks.stream()
                .map(chunk -> CompletableFuture.runAsync(() -> processChunk(chunk, videoId), taskExecutor)
                        .thenRun(() -> logProgress(processedChunks.incrementAndGet(), chunks.size(),videoId))
                )
                .toArray(CompletableFuture[]::new);


        CompletableFuture.allOf(futures).thenRunAsync(() -> {
            try {
                File combinedVideo = videoCombiningService.combineChunks(chunks, videoId, fileName);

                if (combinedVideo.exists()) {
                    logger.info("Combined video saved at: {}", combinedVideo.getAbsolutePath());

                    tempFile.delete();
                    File chunkDir = new File(CHUNK_DIR, videoId);
                    deleteFileOrDirectory(chunkDir);
                    logger.info("Temporary files and chunk directory deleted.");
                } else {
                    logger.error("Failed to combine video.");
                }
            } catch (Exception e) {
                logger.error("Error occurred during video combination.", e);
            }
        }, taskExecutor);
    }

    @Async
    public void processChunk(File chunk, String videoId) {
        try {
            logger.info("Processing chunk: {} for video ID: {}", chunk.getName(), videoId);

             Thread.sleep(1000);

        } catch (Exception e) {
            logger.error("Error processing chunk: {}", chunk.getName(), e);
        }
    }

    private void logProgress(int completedChunks, int totalChunks,String videoId) {
        int progressPercentage = (int) ((completedChunks / (float) totalChunks) * 100);
        logger.info("Progress: {}% done for video ID: {}", progressPercentage, videoId);
    }

    private File saveFileToDirectory(MultipartFile file) throws IOException {

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File savedFile = new File(UPLOAD_DIR, Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(savedFile);
        System.out.println(savedFile.getAbsolutePath());
        return savedFile;
    }

    private void deleteFileOrDirectory(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File childFile : Objects.requireNonNull(fileOrDirectory.listFiles())) {
                deleteFileOrDirectory(childFile);
            }
        }
        fileOrDirectory.delete();
        logger.info("Deleted: {}", fileOrDirectory.getAbsolutePath());
    }
}

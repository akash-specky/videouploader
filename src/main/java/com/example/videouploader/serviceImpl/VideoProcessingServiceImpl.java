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
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.videouploader.utility.Constant.*;

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

         processChunksAsync(chunks, videoId, file.getOriginalFilename(), tempFile);

        return videoId;
    }

    private void processChunksAsync(List<File> chunks, String videoId, String fileName, File tempFile) {
        for (File chunk : chunks) {
            taskExecutor.execute(() -> processChunk(chunk, videoId));
        }

        AtomicBoolean isCombinedSuccessfully = new AtomicBoolean(false);

        taskExecutor.execute(() -> {
            try {
                File combinedVideo = videoCombiningService.combineChunks(chunks, videoId, fileName);
                isCombinedSuccessfully.set(combinedVideo.exists());

                if (isCombinedSuccessfully.get()) {
                    System.out.println("Combined video saved at: " + combinedVideo.getAbsolutePath());

                    tempFile.delete();
                    File chunkDir = new File(CHUNK_DIR, videoId);
                    deleteFileOrDirectory(chunkDir);
                    System.out.println("Temporary files and chunk directory deleted.");
                } else {
                    System.out.println("Failed to combine video.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error occurred during video combination.");
            }
        });
    }

    private void processChunk(File chunk, String videoId) {
        try {
            System.out.println("Processing chunk: " + chunk.getName() + " for video ID: " + videoId);
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

    private void deleteFileOrDirectory(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File childFile : Objects.requireNonNull(fileOrDirectory.listFiles())) {
                deleteFileOrDirectory(childFile);
            }
        }
        fileOrDirectory.delete();
        System.out.println("Deleted: " + fileOrDirectory.getAbsolutePath());
    }
}

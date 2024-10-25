package com.example.videouploader.serviceImpl;

import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.dtos.PaginationDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.model.VideoProperties;
import com.example.videouploader.repository.VideoDetailsRepository;
import com.example.videouploader.service.VideoChunkingService;
import com.example.videouploader.service.VideoCombiningService;
import com.example.videouploader.service.VideoProcessingService;

import com.example.videouploader.utility.CustomSequences;
import org.mp4parser.IsoFile;
import org.mp4parser.boxes.iso14496.part12.MovieHeaderBox;
import org.mp4parser.boxes.iso14496.part12.TrackBox;
import org.mp4parser.boxes.sampleentry.VisualSampleEntry;
import org.mp4parser.tools.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
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

    @Autowired
    CustomSequences sequences;


    private static final Logger logger = LoggerFactory.getLogger(VideoProcessingServiceImpl.class);

    @Autowired
    private VideoDetailsRepository videoDetailsRepository;

    @Override
    public String processUploadedVideo(MultipartFile file) throws Exception {
        extractVideoProperties(file);

        String videoId = UUID.randomUUID().toString();
        return videoId;
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

        if (videoDetailsList.isEmpty()) {
            throw new VideoException("No Videos is availible");
        }
        return videoDetailsList;
    }

    @Override
    public PaginatedResponse getAllVideosWithPagination(PaginationDTO paginationDTO) throws VideoException {
        Pageable pageable = PageRequest.of(paginationDTO.getPageNo(), paginationDTO.getSize());
        Page<VideoDetails> videoPage = videoDetailsRepository.findAll(pageable);

        if (videoPage.isEmpty()) {
            throw new VideoException("No Videos is availible");
        }

        return new PaginatedResponse(
                videoPage.getContent(),
                videoPage.getTotalElements(),
                videoPage.getTotalPages(),
                paginationDTO.getPageNo(),
                paginationDTO.getSize()
        );
    }

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
                        .thenRun(() -> logProgress(processedChunks.incrementAndGet(), chunks.size(), videoId))
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

    private void logProgress(int completedChunks, int totalChunks, String videoId) {
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

    public void extractVideoProperties(MultipartFile multipartFile) throws IOException {


        File file = convertMultipartFileToFile(multipartFile);

        IsoFile isoFile = new IsoFile(file);

        // Extract video duration and fps
        MovieHeaderBox header = Path.getPath(isoFile, "moov/mvhd");
        long duration = header.getDuration();
        long timescale = header.getTimescale();
        float fps = timescale / (float) duration;

        List<TrackBox> trackBoxes = isoFile.getMovieBox().getBoxes(TrackBox.class);

        String codec = "";
        for (TrackBox trackBox : trackBoxes) {

            if (trackBox.getTrackHeaderBox().getWidth() > 0) {
                VisualSampleEntry visualSampleEntry = Path.getPath(trackBox, "mdia/minf/stbl/stsd/avc1");
                if (visualSampleEntry != null) {
                    codec = visualSampleEntry.getType();
                }
            }
        }
        String format = file.getName().substring(file.getName().lastIndexOf('.') + 1);
        VideoProperties videoProperties = new VideoProperties(multipartFile.getSize(), codec, format, fps);

        VideoDetails videoDetails = new VideoDetails();
        videoDetails.setId(sequences.getNextSequence("videoDetails"));
        videoDetails.setVideoProperties(videoProperties);
        videoDetailsRepository.save(videoDetails);

    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}

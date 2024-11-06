package com.example.videouploader.serviceImpl;

import com.example.videouploader.Exception.CustomVideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.model.*;
import com.example.videouploader.repository.VideoDetailsRepository;
import com.example.videouploader.service.VideoChunkingService;
import com.example.videouploader.service.VideoCombiningService;
import com.example.videouploader.service.VideoProcessingService;
import com.example.videouploader.utility.CustomSequences;
import com.example.videouploader.utility.EntityToResponseConverter;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameGrabber.Exception;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    CustomSequences sequences;


    @Autowired
    VideoDetailsRepository videoDetailsRepository;

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private VideoChunkingService videoChunkingService;

    @Autowired
    private VideoCombiningService videoCombiningService;


    private static final Logger logger = LoggerFactory.getLogger(VideoProcessingServiceImpl.class);




    @Override
    public VideoDetailsResponse getVideoById(Integer id) throws CustomVideoException {

            Optional<VideoDetails> optionalVideoDetails = videoDetailsRepository.findById(id);
            if (optionalVideoDetails.isPresent()) {
                VideoDetails videoDetails = optionalVideoDetails.get();
                return EntityToResponseConverter.convertEntityToResponse(videoDetails);
            }
            throw new CustomVideoException("Invalid video details!");

    }



    @Override
    public List<VideoDetailsResponse> getAllVideos() throws CustomVideoException {

        List<VideoDetails> videoDetailsList = videoDetailsRepository.findAll();
        if (videoDetailsList.isEmpty()){
            throw new CustomVideoException("No Videos is availible");
        }
        return EntityToResponseConverter.convertListOfEntityToResponse(videoDetailsList);
    }


    @Override
    public PaginatedResponse getAllVideosWithPagination(PaginationDTO paginationDTO) throws CustomVideoException {

        Pageable pageable = PageRequest.of(paginationDTO.getPageNo()-1, paginationDTO.getSize());
        Page<VideoDetails> videoPage = videoDetailsRepository.findAll(pageable);

        if (videoPage.isEmpty()){
            throw  new CustomVideoException("No videos found for the requested page.");
        }
        List<VideoDetailsResponse> videoDetailsResponseList = EntityToResponseConverter.convertListOfEntityToResponse(videoPage.getContent());

        PaginatedResponse paginatedResponse = new PaginatedResponseBuilder()
                .setVideoDetailsResponseList(videoDetailsResponseList)
                .setTotalElements(videoPage.getTotalElements())
                .setTotalPages(videoPage.getTotalPages())
                .setPageNo(paginationDTO.getPageNo())
                .setSize(paginationDTO.getSize())
                .build();
        return paginatedResponse;
    }




    @Override
    public String uploadThumbnail(MultipartFile file, Integer videoId) {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new RuntimeException("Only JPG and PNG files are accepted.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size must be less than 500KB.");
        }

        String originalFileName = System.currentTimeMillis() + "_original." + extension;
        File outputDir = new File(THUMBNAIL_DIR);
        if (!outputDir.exists()){
            outputDir.mkdirs();
        }
        File savedFile = new File(THUMBNAIL_DIR, originalFileName);

        try (FileOutputStream fos = new FileOutputStream(savedFile)) {
            fos.write(file.getBytes());

            // Create and save thumbnails
            String small =  createThumbnail(savedFile, SMALL_WIDTH, SMALL_HEIGHT, "small");
            String medium = createThumbnail(savedFile, MEDIUM_WIDTH, MEDIUM_HEIGHT, "medium");
            String large = createThumbnail(savedFile, LARGE_WIDTH, LARGE_HEIGHT, "large");


            Map<String, String> thumbnails = new HashMap<>();
            thumbnails.put("Small", small);
            thumbnails.put("Medium", medium);
            thumbnails.put("Large", large);

            Optional<VideoDetails> optionalVideoDetails = videoDetailsRepository.findById(videoId);
            if (optionalVideoDetails.isEmpty()){
                throw new CustomVideoException("Invalid videoId!");
            }
            VideoDetails videoDetails = optionalVideoDetails.get();
            videoDetails.setVideoThumbnails(thumbnails);
            videoDetailsRepository.save(videoDetails);

            return "Thumbnails uploaded successfully: ";
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CustomVideoException e) {
            throw new RuntimeException(e);
        }
    }




    private String createThumbnail(File file, int width, int height, String size) throws IOException {

        String baseName = FilenameUtils.getBaseName(file.getName());
        String extension = FilenameUtils.getExtension(file.getName());

        try {
            String fileName = baseName+"_" + size + "." + extension;
            File outputFile = new File(THUMBNAIL_DIR +File.separator+ fileName);

            // Use the temp file for creating thumbnails
            Thumbnails.of(file)
                    .size(width, height)
                    .toFile(outputFile);
            return fileName;

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }




    @Override
    public String processUploadedVideo(MultipartFile file, String resolution) throws Exception {
        File tempFile = null;
        try {
//            tempFile = saveFileToDirectory(file);
            extractVideoProperties(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String videoId = UUID.randomUUID().toString();
//        List<File> chunks = null;
//        try {
//            chunks = videoChunkingService.chunkVideo(tempFile, videoId, resolution);
//        } catch (java.lang.Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        processChunksAsync(chunks, videoId, file.getOriginalFilename(), tempFile);

        return videoId;
    }



    @Async
    public void processChunksAsync(List<File> chunks, String videoId, String fileName, File tempFile) {
        AtomicInteger processedChunks = new AtomicInteger(0);

        CompletableFuture[] futures = chunks.stream()
                .map(chunk -> CompletableFuture.runAsync(() -> {
                                    try {
                                        processChunk(chunk, videoId);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }, taskExecutor)
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
                e.printStackTrace();
            } catch (java.lang.Exception e) {
                logger.error("Error occurred during video combination.", e);
                throw new RuntimeException(e);

            }
        }, taskExecutor);
    }

    @Async
    public void processChunk(File chunk, String videoId) throws InterruptedException {
        try {
            logger.info("Processing chunk: {} for video ID: {}", chunk.getName(), videoId);

            System.out.println("Processing chunk: " + chunk.getName()+"  "+videoId);
        } catch (Throwable e) {
            System.err.println("Error processing chunk: " + chunk.getName());
            e.printStackTrace();
            Thread.sleep(1000);

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

        try {
            File videoFile = File.createTempFile("temp-video", multipartFile.getOriginalFilename());
            multipartFile.transferTo(videoFile);

            String fileNme = file.getName();

            Path filePath = Paths.get(videoFile.getAbsolutePath());
            BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);

            LocalDateTime createdDate = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime modifiedDate = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());

            // Use FFmpegFrameGrabber to extract video properties
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile);
            grabber.start();

            long duration = grabber.getLengthInTime();
            duration = duration/1000000;                //convert into second
            int frameWidth = grabber.getImageWidth();
            int frameHeight = grabber.getImageHeight();
            String codec = grabber.getVideoCodecName();
            double frameRate = grabber.getFrameRate();
            double fps = grabber.getFrameRate();
            long fileSize = Files.size(filePath);
            System.out.println(grabber.getMetadata());
            System.out.println(grabber.getVideoMetadata());

            String format = file.getName().substring(file.getName().lastIndexOf('.') + 1);

            if (fps <= 0) {
                int frameCount = 0;
                long startTime = System.currentTimeMillis();
                while (true) {
                    grabber.grab();
                    frameCount++;
                    if (System.currentTimeMillis() - startTime >= 1000) {
                        break;
                    }
                }
                fps = frameCount;
            }

            VideoProperties videoProperties = new VideoProperties(sequences.getNextSequence("videoProperties"), frameWidth, frameHeight, frameRate, createdDate, modifiedDate);
            VideoDetails videoDetails = new VideoDetails(sequences.getNextSequence("videoDetails"), UPLOAD_DIR, fileNme, duration,multipartFile.getSize(), codec,format, fps, false, LocalDateTime.now(), LocalDateTime.now(), videoProperties);
            videoDetailsRepository.save(videoDetails);

            // Clean up
            grabber.stop();
            videoFile.delete();

        } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }

    }


    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }


    public static void convertVideo(File inputFilePath, String outputFilePath, int width, int height) {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFilePath);
             FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFilePath, width, height)) {

            // Set formats and start grabber and recorder
            grabber.start();
            recorder.setVideoCodec(grabber.getVideoCodec());
            recorder.setFormat(grabber.getFormat());
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setVideoBitrate(grabber.getVideoBitrate());
            recorder.setVideoQuality(0);  // Highest quality

            recorder.start();

            // Read frames and write to the output
            Frame frame;
            while ((frame = grabber.grab()) != null) {
                recorder.record(frame);
            }

            // Stop everything
            recorder.stop();
            grabber.stop();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to convert video to " + width + "x" + height);
        }
    }




    @Async
    @Override
    public String processUnconvertedVideos() throws IOException {
        File videoDir = new File(VIDEO_DIR);
        if (!videoDir.exists()) {
            return "Video directory does not exist.";
        }

        List<VideoDetails> videoDetailsList = videoDetailsRepository.findAll();

        for (VideoDetails videoDetails:videoDetailsList){
            if (!videoDetails.isResolutiosAvailable()){
                if (videoDetails.getFileName()!=null){
                    File videoFile = new File(VIDEO_DIR, videoDetails.getFileName());
                    Map<Integer, String> videoResolutios =  processVideo(videoFile);
                    videoDetails.setResolutiosAvailable(true);
                    videoDetails.setVideoResolutions(videoResolutios);
                    videoDetailsRepository.save(videoDetails);
                }
            }
        }
        return "Videos Convertad Successfully!";
    }

    private Map<Integer, String> processVideo(File video) throws IOException {

        File outputFolder = new File(OUTPUT_VIDEO_DIR );
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        int pixcel = getVideoPixcel(video);

        // Resolutions to convert
        int[] heights = {144, 240, 480, 720, 1080};
        int[] widths = {256, 426, 854, 1280, 1920};

        Map<Integer, String> videosMap = new HashMap<>();

        for (int i=0; i<5; i++) {
            if (pixcel!=heights[i]) {
                String videoName = System.currentTimeMillis()+"_"+heights[i]+".mp4";
                convertVideo(video, OUTPUT_VIDEO_DIR+"/"+videoName , widths[i], heights[i]);
                videosMap.put(heights[i],videoName);
            }
        }

        return videosMap;
    }




    public static int getVideoPixcel(File videoFile) throws FFmpegFrameGrabber.Exception {

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile);
        grabber.start();
        return grabber.getImageHeight();
    }

}

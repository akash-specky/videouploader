package com.example.videouploader.serviceImpl;

import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.dto.PaginationDTO;
import com.example.videouploader.model.PaginatedResponse;
import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.model.VideoProperties;
import com.example.videouploader.repository.VideoDetailsRepository;
import com.example.videouploader.repository.VideoPropertiesRepository;
import com.example.videouploader.service.VideoChunkingService;
import com.example.videouploader.service.VideoCombiningService;
import com.example.videouploader.service.VideoProcessingService;
import com.example.videouploader.utility.CustomSequences;
import jakarta.annotation.PostConstruct;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executor;

import static com.example.videouploader.utility.Constant.UPLOAD_DIR;


@Service
public class VideoProcessingServiceImpl implements VideoProcessingService {


    private static final long MAX_FILE_SIZE = 500 * 1024; // 500KB

    // Folder to save thumbnails
    private static final String UPLOAD_DIR = "uploads/thumbnails/";

    // Define thumbnail sizes (small, medium, large)
    private static final int SMALL_WIDTH = 100;
    private static final int SMALL_HEIGHT = 100;

    private static final int MEDIUM_WIDTH = 300;
    private static final int MEDIUM_HEIGHT = 300;

    private static final int LARGE_WIDTH = 600;
    private static final int LARGE_HEIGHT = 600;

    @Autowired
    CustomSequences sequences;

    @Autowired
    VideoPropertiesRepository videoPropertiesRepository;


    private final Tika tika = new Tika();


    @Autowired
    VideoDetailsRepository videoDetailsRepository;

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private VideoChunkingService videoChunkingService;

    @Autowired
    private VideoCombiningService videoCombiningService;





    @Override
    public String processUploadedVideo(MultipartFile file) throws IOException {

//        File tempFile = saveFileToDirectory(file);

        extractVideoProperties(file);

        String videoId = UUID.randomUUID().toString();
//        List<File> chunks = videoChunkingService.chunkVideo(tempFile, videoId);
//        processChunksAsync(chunks, videoId);

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

        if (videoDetailsList.isEmpty()){
            throw new VideoException("No Videos is availible");
        }
        return videoDetailsList;
    }

    @Override
    public PaginatedResponse getAllVideosWithPagination(PaginationDTO paginationDTO) throws VideoException {

        if (paginationDTO.getPageNo()>0){
            Pageable pageable = PageRequest.of(paginationDTO.getPageNo()-1, paginationDTO.getSize());
            Page<VideoDetails> videoPage = videoDetailsRepository.findAll(pageable);

            if (videoPage.isEmpty()){
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
        throw new RuntimeException("invalid pageNo");

    }

    @Override
    public String uploadThumbnail(MultipartFile file) {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPG and PNG files are accepted.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size must be less than 500KB.");
        }

        String originalFileName = System.currentTimeMillis() + "_original." + extension;
        File savedFile = new File("c://Users/gaurav.singh/Desktop/videoUploaderTask/thumnails", originalFileName);

        try (FileOutputStream fos = new FileOutputStream(savedFile)) {
            fos.write(file.getBytes());

            // Create and save thumbnails
            createThumbnail(savedFile, SMALL_WIDTH, SMALL_HEIGHT, "small");
            createThumbnail(savedFile, MEDIUM_WIDTH, MEDIUM_HEIGHT, "medium");
            createThumbnail(savedFile, LARGE_WIDTH, LARGE_HEIGHT, "large");

            return "Thumbnails uploaded successfully: " + originalFileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload thumbnails.");
        }
    }


    private void createThumbnail(File file, int width, int height, String size) throws IOException {

        String extension = FilenameUtils.getExtension(file.getName());
//        File tempFile = File.createTempFile("upload_", "_" + size + "." + extension);
//
        try {
            // Write the MultipartFile content to the temporary file
//            FileOutputStream fos = new FileOutputStream(tempFile);
//            byte[] bytes = file.getBytes();
//            fos.write(bytes);

            // Determine the output file path for resized thumbnails
            String fileName = System.currentTimeMillis() + "_" + size + "." + extension;
            File outputFile = new File("c://Users/gaurav.singh/Desktop/videoUploaderTask/thumnails" + fileName);

            // Use the temp file for creating thumbnails
            Thumbnails.of(file)
                    .size(width, height)
                    .toFile(outputFile);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
//        finally {
        // Delete the temporary file after processing
//            if (tempFile.exists()) {
//                tempFile.delete();
//            }
//    }

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
            } catch (java.lang.Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void processChunk(File chunk, String videoId) {
        try {

            System.out.println("Processing chunk: " + chunk.getName()+"  "+videoId);
        } catch (Throwable e) {
            System.err.println("Error processing chunk: " + chunk.getName());
            e.printStackTrace();
        }
    }

    private File saveFileToDirectory(MultipartFile file) throws IOException {



        File savedFile = new File(UPLOAD_DIR, Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(savedFile);

        return savedFile;
    }



    public void extractVideoProperties(MultipartFile multipartFile) throws IOException {

        // Convert MultipartFile to a File
        File file = convertMultipartFileToFile(multipartFile);

        try {
            // Convert MultipartFile to a temporary file
            File videoFile = File.createTempFile("temp-video", multipartFile.getOriginalFilename());
            multipartFile.transferTo(videoFile);

            // Get created and modified dates
            Path filePath = Paths.get(videoFile.getAbsolutePath());
            BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);

            LocalDateTime createdDate = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
            LocalDateTime modifiedDate = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());

            // Use FFmpegFrameGrabber to extract video properties
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFile);
            grabber.start();

            // Extract properties
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

            VideoProperties videoProperties = new VideoProperties
                    (sequences.getNextSequence("videoProperties"), frameWidth, frameHeight, frameRate, createdDate, modifiedDate);

            VideoDetails videoDetails = new VideoDetails(sequences.getNextSequence("videoDetails"), multipartFile.getSize(), codec,format, fps,videoProperties);
//            videoDetails.setId(sequences.getNextSequence("videoDetails"));
//            videoDetails.setVideoProperties(videoProperties);
            videoDetailsRepository.save(videoDetails);

            // Clean up
            grabber.stop();
            videoFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        IsoFile isoFile = new IsoFile(file);
//
//        // Extract video duration and fps
//        MovieHeaderBox header = Path.getPath(isoFile, "moov/mvhd");
//        long duration = header.getDuration();
//        long timescale = header.getTimescale();
//        float fps = timescale / (float) duration;
//
//        List<TrackBox> trackBoxes = isoFile.getMovieBox().getBoxes(TrackBox.class);
//
////        String codec = Path.getPath(trackBoxes.get(0), "mdia/minf/stbl/stsd/avc1").getType();
//        String codec = "";
//        for (TrackBox trackBox : trackBoxes) {
//
//            if (trackBox.getTrackHeaderBox().getWidth() > 0) {
//                VisualSampleEntry visualSampleEntry = Path.getPath(trackBox, "mdia/minf/stbl/stsd/avc1");
//                if (visualSampleEntry != null) {
//                    codec = visualSampleEntry.getType();
//                }
//            }
//        }
//        String format = file.getName().substring(file.getName().lastIndexOf('.') + 1);
//        if (!format.equalsIgnoreCase("mp4")){
//            throw new IllegalArgumentException("Invalid Video format exception");
//        }

//        VideoProperties videoProperties = new VideoProperties
//                (sequences.getNextSequence("videoProperties"), multipartFile.getSize(), codec,format, fps);
//
//        VideoDetails videoDetails = new VideoDetails();
//        videoDetails.setId(sequences.getNextSequence("videoDetails"));
//        videoDetails.setVideoProperties(videoProperties);
//        videoDetailsRepository.save(videoDetails);

    }

    // Utility method to convert MultipartFile to File
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }



//    @Async
    @Override
    public String convertVideosAsync() throws IOException {


        String inputFilePath = "c://Users/gaurav.singh/Downloads/animal.mp4";
        String outputFilePath = "videos/output_144p.mp4";
        // Convert the video to different resolutions
        convertVideo(inputFilePath, "Thumbnail//output_144p.mp4", 256, 144);
        convertVideo(inputFilePath, "Thumbnail//output_240p.mp4", 426, 240);
        convertVideo(inputFilePath, "Thumbnail//output_480p.mp4", 854, 480);
        convertVideo(inputFilePath, "Thumbnail//output_720p.mp4", 1280, 720);
        return  "resolution Successfully!";
    }


    public static void convertVideo(String inputFilePath, String outputFilePath, int width, int height) {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFilePath);
             FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFilePath, width, height)) {

            // Set formats and start grabber and recorder
            grabber.start();
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("mp4");
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

            System.out.println("Converted " + outputFilePath + " successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to convert video to " + width + "x" + height);
        }
    }


}

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
import org.apache.tika.Tika;
import org.mp4parser.IsoFile;
import org.mp4parser.boxes.iso14496.part12.MovieHeaderBox;
import org.mp4parser.boxes.iso14496.part12.TrackBox;
import org.mp4parser.boxes.sampleentry.VisualSampleEntry;
import org.mp4parser.tools.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;

import static com.example.videouploader.utility.Constant.UPLOAD_DIR;


@Service
public class VideoProcessingServiceImpl implements VideoProcessingService {


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
    public String processUploadedVideo(MultipartFile file) throws Exception {

//        File tempFile = saveFileToDirectory(file);

        extractVideoProperties(file);

        String videoId = UUID.randomUUID().toString();
//        List<File> chunks = videoChunkingService.chunkVideo(tempFile, videoId);
//        processChunksAsync(chunks, videoId);

        return videoId;
    }

//    @Override
//    public String saveVideoProperties(String videoPath, VideoProperties properties) throws VideoException {
//
////        File file = new File(videoPath);
//
////        if (!file.exists()){
////            throw new IllegalArgumentException("Invalid file path");
////        }
////        if (!properties.getFormat().equalsIgnoreCase("mp4")){
////            throw new IllegalArgumentException("Invalid Video format exception");
////        }
//
//        try{
//            VideoDetails videoDetails = new VideoDetails();
//            videoDetails.setId(sequences.getNextSequence("videoDetails"));
//            videoDetails.setPath(videoPath);
//            videoDetails.setVideoProperties(properties);
//            videoDetailsRepository.save(videoDetails);
//
//            return "Properties saved successfully!";
//
//        }catch (Exception e){
//            throw new VideoException("Invalid video details!");
//        }
//
//    }

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

        Pageable pageable = PageRequest.of(paginationDTO.getPageNo(), paginationDTO.getSize());
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



    public void extractVideoProperties(MultipartFile multipartFile) throws IOException {

        // Convert MultipartFile to a File
        File file = convertMultipartFileToFile(multipartFile);

        IsoFile isoFile = new IsoFile(file);

        // Extract video duration and fps
        MovieHeaderBox header = Path.getPath(isoFile, "moov/mvhd");
        long duration = header.getDuration();
        long timescale = header.getTimescale();
        float fps = timescale / (float) duration;

        List<TrackBox> trackBoxes = isoFile.getMovieBox().getBoxes(TrackBox.class);

//        String codec = Path.getPath(trackBoxes.get(0), "mdia/minf/stbl/stsd/avc1").getType();
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
        VideoProperties videoProperties = new VideoProperties(multipartFile.getSize(), codec,format, fps);

        VideoDetails videoDetails = new VideoDetails();
        videoDetails.setId(sequences.getNextSequence("videoDetails"));
        videoDetails.setVideoProperties(videoProperties);
        videoDetailsRepository.save(videoDetails);

    }

    // Utility method to convert MultipartFile to File
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }


    }

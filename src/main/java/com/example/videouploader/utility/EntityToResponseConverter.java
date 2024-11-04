package com.example.videouploader.utility;

import com.example.videouploader.model.VideoDetails;
import com.example.videouploader.model.VideoDetailsResponse;
import com.example.videouploader.model.VideoProperties;
import com.example.videouploader.model.VideoPropertiesResponse;

import java.util.ArrayList;
import java.util.List;

public class EntityToResponseConverter {

    public static VideoDetailsResponse convertEntityToResponse(VideoDetails videoDetails){

        VideoPropertiesResponse videoPropertiesResponse = new VideoPropertiesResponse();
        VideoDetailsResponse videoDetailsResponse = new VideoDetailsResponse();
        videoDetailsResponse.setId(videoDetails.getId());
        videoDetailsResponse.setPath(videoDetails.getPath());
        videoDetailsResponse.setFileName(videoDetails.getFileName());
        videoDetailsResponse.setDuration(videoDetails.getDuration());
        videoDetailsResponse.setSize(videoDetails.getSize());
        videoDetailsResponse.setFormat(videoDetails.getFormat());
        videoDetailsResponse.setCodec(videoDetails.getCodec());
        videoDetailsResponse.setFps(videoDetails.getFps());
        videoDetailsResponse.setVideoResolutions(videoDetails.getVideoResolutions());
        videoDetailsResponse.setVideoThumbnails(videoDetails.getVideoThumbnails());

        VideoProperties videoProperties = videoDetails.getVideoProperties();
        videoPropertiesResponse.setId(videoProperties.getId());
        videoPropertiesResponse.setFrameHeight(videoProperties.getFrameHeight());
        videoPropertiesResponse.setFrameWidth(videoProperties.getFrameWidth());
        videoPropertiesResponse.setFrameRate(videoProperties.getFrameRate());
        videoPropertiesResponse.setCreatedDate(videoProperties.getCreatedDate());
        videoPropertiesResponse.setModifiedDate(videoProperties.getModifiedDate());
        videoDetailsResponse.setVideoPropertiesResponse(videoPropertiesResponse);
        return videoDetailsResponse;

    }

    public static List<VideoDetailsResponse> convertListOfEntityToResponse(List<VideoDetails> videoDetailsList){

        List<VideoDetailsResponse> videoDetailsResponseList = new ArrayList<>();

        for (VideoDetails videoDetails: videoDetailsList){
            VideoPropertiesResponse videoPropertiesResponse = new VideoPropertiesResponse();
            VideoDetailsResponse videoDetailsResponse = new VideoDetailsResponse();
            videoDetailsResponse.setId(videoDetails.getId());
            videoDetailsResponse.setPath(videoDetails.getPath());
            videoDetailsResponse.setFileName(videoDetails.getFileName());
            videoDetailsResponse.setDuration(videoDetails.getDuration());
            videoDetailsResponse.setSize(videoDetails.getSize());
            videoDetailsResponse.setFormat(videoDetails.getFormat());
            videoDetailsResponse.setCodec(videoDetails.getCodec());
            videoDetailsResponse.setFps(videoDetails.getFps());
            videoDetailsResponse.setVideoResolutions(videoDetails.getVideoResolutions());
            videoDetailsResponse.setVideoThumbnails(videoDetails.getVideoThumbnails());

            VideoProperties videoProperties = videoDetails.getVideoProperties();
            videoPropertiesResponse.setId(videoProperties.getId());
            videoPropertiesResponse.setFrameHeight(videoProperties.getFrameHeight());
            videoPropertiesResponse.setFrameWidth(videoProperties.getFrameWidth());
            videoPropertiesResponse.setFrameRate(videoProperties.getFrameRate());
            videoPropertiesResponse.setCreatedDate(videoProperties.getCreatedDate());
            videoPropertiesResponse.setModifiedDate(videoProperties.getModifiedDate());
            videoDetailsResponse.setVideoPropertiesResponse(videoPropertiesResponse);

            videoDetailsResponseList.add(videoDetailsResponse);
        }
        return videoDetailsResponseList;

    }
}

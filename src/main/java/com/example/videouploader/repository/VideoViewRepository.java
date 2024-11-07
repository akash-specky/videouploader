package com.example.videouploader.repository;

import com.example.videouploader.model.VideoView;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VideoViewRepository extends MongoRepository<VideoView, String> {
    Optional<VideoView> findByVideoIdAndIpAddressAndDeviceId(Long videoId, String ipAddress, String deviceId);
}

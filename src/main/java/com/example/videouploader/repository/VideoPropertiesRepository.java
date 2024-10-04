package com.example.videouploader.repository;


import com.example.videouploader.model.VideoProperties;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoPropertiesRepository extends MongoRepository<VideoProperties, Long> {
}

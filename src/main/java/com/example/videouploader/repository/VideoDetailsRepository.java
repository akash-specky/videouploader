package com.example.videouploader.repository;

import com.example.videouploader.model.VideoDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDetailsRepository extends MongoRepository<VideoDetails, Integer> {



}

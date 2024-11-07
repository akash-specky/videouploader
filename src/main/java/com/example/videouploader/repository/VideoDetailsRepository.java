package com.example.videouploader.repository;

import com.example.videouploader.model.VideoDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoDetailsRepository extends MongoRepository<VideoDetails, Integer> {

    VideoDetails findById(Long id);

    List<VideoDetails> findAllByPublishedTrue();

    @Query(value = "{}", fields = "{ 'format' : 1 }")
    List<String> findDistinctFormats();

    @Query(value = "{}", fields = "{ 'duration' : 1 }")
    List<Long> findDistinctDurations();

    @Query(value = "{ 'videoProperties.frameHeight': { $exists: true } }", fields = "{ 'videoProperties.frameHeight' : 1 }")
    List<Integer> findDistinctFrameHeights();

}


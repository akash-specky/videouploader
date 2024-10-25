package com.example.videouploader.repository;

import com.example.videouploader.dtos.FilterParams;
import com.example.videouploader.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface VideoRepository extends MongoRepository<Video, String> {

    @Query("{ $and: [ " +
            "{ 'duration': { $gte: ?0, $lte: ?1 } }, " +
            "{ 'format': ?2 }, " +
            "{ 'uploadTime': { $gte: ?3, $lte: ?4 } } " +
            "] }")
    List<Video> findVideosByFilter(
            Integer durationFrom,
            Integer durationTo,
            String format,
            Date uploadTimeFrom,
            Date uploadTimeTo
    );
}

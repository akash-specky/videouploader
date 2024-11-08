package com.example.videouploader.repository;

import com.example.videouploader.model.WatchList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListRepository extends MongoRepository<WatchList, String> {
    List<WatchList> findByUserId(String userId);
    boolean existsByUserIdAndVideoId(String userId, String videoId);
}
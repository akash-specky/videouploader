package com.example.videouploader.repository;

import com.example.videouploader.model.WatchList;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface WatchListRepository extends MongoRepository<WatchList, String> {
    List<WatchList> findByUserId(String userId);
    boolean existsByUserIdAndVideoId(String userId, String videoId);
}
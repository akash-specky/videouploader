package com.example.videouploader.repository;

import com.example.videouploader.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    Optional<Like> findByUserIdAndVideoId(String userId, String videoId);
    long countByVideoId(String videoId);
}

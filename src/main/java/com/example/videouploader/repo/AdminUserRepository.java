package com.example.videouploader.repo;

import com.example.videouploader.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserRepository extends MongoRepository<User, String> {
    List<User> findByNameContainingIgnoreCase(String name);

    long countBySsoIdNotNull();
}

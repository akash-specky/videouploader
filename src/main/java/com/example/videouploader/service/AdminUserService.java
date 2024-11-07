package com.example.videouploader.service;

import com.example.videouploader.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface AdminUserService {


    User createUser(User user);

    Optional<User> updateUser(String id, User userDetails);

    void deleteUser(String id);

    Optional<User> findUserById(String id);

    long countActiveUsers();

    List<User> findUsersByName(String name);
}

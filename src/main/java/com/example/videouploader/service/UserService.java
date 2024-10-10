package com.example.videouploader.service;


import com.example.videouploader.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User registerUser(User user) throws Exception ;

}

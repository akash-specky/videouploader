package com.example.videouploader.service;


import com.example.videouploader.exceptions.UserAlreadyExist;
import com.example.videouploader.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User registerUser(User user) throws Exception ;
    public User registerUser(OAuth2User principal) throws UserAlreadyExist;

}

package com.example.videouploader.security;

import com.example.videouploader.model.User;
import com.example.videouploader.model.UserLoginRequest;
import com.example.videouploader.repo.UserRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenGenerator tokenGenerator;

    @Autowired
    PasswordEncoder passwordEncoder;


    public String authenticate(UserLoginRequest userLoginRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(userLoginRequest.getEmail());
        if (optionalUser.isEmpty()){
            throw new RuntimeException("Invalid email id");
        }
        User user = optionalUser.get();
        if (user.getEmail().equals(userLoginRequest.getEmail()) && passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            return tokenGenerator.generateToken(userLoginRequest.getEmail(), user.getName(), user.getRole());
        }
        throw new RuntimeException("Invalid email or password");
    }
}


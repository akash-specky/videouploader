package com.example.videouploader.serviceImpl;

import com.example.videouploader.model.User;
import com.example.videouploader.repo.UserRepository;
import com.example.videouploader.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User registerUser(User user) throws Exception {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email already exists");
        }

        if (userRepository.findByMobileNo(user.getMobileNo()).isPresent()) {
            throw new Exception("Mobile number already exists");
        }

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {

            user.setPassword(null);
        }


        return userRepository.save(user);
    }
}

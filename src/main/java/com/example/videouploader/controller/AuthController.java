package com.example.videouploader.controller;

import com.example.videouploader.Exception.CustomVideoException;
import com.example.videouploader.dtos.CommonResponseDTO;
import com.example.videouploader.model.User;
import com.example.videouploader.model.UserLoginRequest;
import com.example.videouploader.model.VideoDetailsResponse;
import com.example.videouploader.repo.UserRepository;
import com.example.videouploader.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody UserLoginRequest userLoginRequest) {
        String token = authenticationService.authenticate(userLoginRequest);
        return ResponseEntity.ok(token);

//        try {
//            return new ResponseEntity<>(new CommonResponseDTO(true, "Successfull!", videoDetailsResponse), HttpStatus.OK);
//        } catch (CustomVideoException e) {
//            return new ResponseEntity<>(new CommonResponseDTO(false, e.getMessage(), new VideoDetailsResponse()), HttpStatus.BAD_REQUEST);
//        }
    }


}

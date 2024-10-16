package com.example.videouploader.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserAlreadyExist extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UserAlreadyExist(String message) {
        super(message);
    }
}

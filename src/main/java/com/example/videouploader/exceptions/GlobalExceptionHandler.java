package com.example.videouploader.exceptions;

import com.example.videouploader.dtos.CommonResponseDTO;
import com.example.videouploader.serviceImpl.VideoProcessingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExist.class)
    public final ResponseEntity<CommonResponseDTO> handleResourceConflictExceptions(Exception ex) {

        log.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new CommonResponseDTO(false, ex.getMessage(), null));
    }
}

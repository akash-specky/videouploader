package com.example.videouploader.Exception;


import com.example.videouploader.dtos.CommonResponseDTO;
import com.example.videouploader.exceptions.UserAlreadyExist;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomVideoException.class)
    public ResponseEntity<MyErrorDetails> UsgerException(CustomVideoException ue, WebRequest wr){

        MyErrorDetails error = new MyErrorDetails();
        error.setLocalDateTime(LocalDateTime.now());
        error.setMsg(ue.getMessage());
        error.setDetails(wr.getDescription(false));

        return new  ResponseEntity<MyErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyErrorDetails> exception(Exception e, WebRequest wr){

        MyErrorDetails error = new MyErrorDetails();
        error.setLocalDateTime(LocalDateTime.now());
        error.setMsg(e.getMessage());
        error.setDetails(wr.getDescription(false));

        return new  ResponseEntity<MyErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MyErrorDetails> orderException(MethodArgumentNotValidException e, WebRequest wr){

        MyErrorDetails error = new MyErrorDetails();
        error.setLocalDateTime(LocalDateTime.now());
        error.setMsg(String.valueOf(e.getBindingResult().getFieldError().getDefaultMessage()));
        error.setDetails(wr.getDescription(false));

        return new  ResponseEntity<MyErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MongoException.class)
    public ResponseEntity<MyErrorDetails> orderException(MongoException e, WebRequest wr){

        MyErrorDetails error = new MyErrorDetails();
        error.setLocalDateTime(LocalDateTime.now());
        error.setMsg(e.getMessage());
        error.setDetails(wr.getDescription(false));

        return new  ResponseEntity<MyErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlreadyExist.class)
    public final ResponseEntity<CommonResponseDTO> handleResourceConflictExceptions(Exception ex) {

        log.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new CommonResponseDTO(false, ex.getMessage(), null));
    }

}
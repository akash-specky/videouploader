package com.example.videouploader.Exception;


import com.mongodb.MongoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VideoException.class)
    public ResponseEntity<MyErrorDetails> UserException(VideoException ue, WebRequest wr){

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


}

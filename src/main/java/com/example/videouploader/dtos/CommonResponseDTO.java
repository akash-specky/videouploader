package com.example.videouploader.dtos;

import java.io.Serializable;



public class CommonResponseDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Boolean isSuccess;
    private String message;
    private transient Object details;


    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public CommonResponseDTO() {
    }

    public CommonResponseDTO(Boolean isSuccess, String message, Object details) {

        this.isSuccess = isSuccess;
        this.message = message;
        this.details = details;
    }

    public CommonResponseDTO(String message2, Object object) {
        this.message = message2;
        this.details = object;
    }


}

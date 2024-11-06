package com.example.videouploader.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Boolean isSuccess;
    private String message;
    private transient Object details;

}

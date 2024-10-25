package com.example.videouploader.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterParams {

    private Integer durationFrom;
    private Integer durationTo;
    private String duration;
    private String format;
    private Date uploadTimeFrom;
    private Date uploadTimeTo;
    private String uploadTime;
}

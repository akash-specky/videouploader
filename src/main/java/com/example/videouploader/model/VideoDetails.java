package com.example.videouploader.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDetails {

    @Id
    Integer id;

    Long size;
    String codec;
    String format;
    double fps;

//    @CreatedDate
//    LocalDateTime createdAt;
//
//    @LastModifiedBy
//    LocalDateTime updatedAt;

    VideoProperties videoProperties;
}

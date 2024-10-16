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

    String path;

    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedBy
    LocalDateTime updatedAt;

    VideoProperties videoProperties;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public VideoProperties getVideoProperties() {
        return videoProperties;
    }

    public void setVideoProperties(VideoProperties videoProperties) {
        this.videoProperties = videoProperties;
    }
}

//public class VideoDetails {
//    @Id
//    Integer id;
//    String path;
//    @CreatedDate
//    LocalDateTime createdAt;
//    @LastModifiedBy
//    LocalDateTime updatedAt;
//    VideoProperties videoProperties;
//}
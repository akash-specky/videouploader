package com.example.videouploader.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "videos")
public class Video {

    @Id
    private String videoId;
    private String title;
    private String format;
    private String duration;
    private Date uploadTime;


    public Video(String invalidInput) {
        this.title = invalidInput;
    }
}

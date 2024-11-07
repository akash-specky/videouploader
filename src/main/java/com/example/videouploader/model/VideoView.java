package com.example.videouploader.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "video_views")
public class VideoView {
    @Id
    private String id;
    private Long videoId;
    private String ipAddress;
    private String deviceId;
    private LocalDateTime viewTime;

}

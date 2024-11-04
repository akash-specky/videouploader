package com.example.videouploader.model;



import lombok.AccessLevel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Document(collection = "watchlists")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WatchList {

    @Id
    String id;
    String userId;
    String videoId;
    Date addedAt = new Date();
}

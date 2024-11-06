package com.example.videouploader.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse {

    private List<VideoDetails> videos;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;


}

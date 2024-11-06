package com.example.videouploader.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse {

    private List<VideoDetailsResponse> videos;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;


    PaginatedResponse(PaginatedResponseBuilder builder) {
        this.videos = builder.videoDetailsResponseList;
        this.totalElements = builder.totalElements;
        this.totalPages = builder.totalPages;
        this.currentPage = builder.pageNo;
        this.pageSize = builder.size;
    }

    public List<VideoDetailsResponse> getVideos() {
        return videos;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}



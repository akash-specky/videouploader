package com.example.videouploader.model;

import java.util.List;

public class PaginatedResponseBuilder {

    List<VideoDetailsResponse> videoDetailsResponseList;
    long totalElements;
    int totalPages;
    int pageNo;
    int size;

    public PaginatedResponseBuilder setVideoDetailsResponseList(List<VideoDetailsResponse> videoDetailsResponseList) {
        this.videoDetailsResponseList = videoDetailsResponseList;
        return this;
    }

    public PaginatedResponseBuilder setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public PaginatedResponseBuilder setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public PaginatedResponseBuilder setPageNo(int pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public PaginatedResponseBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public PaginatedResponse build() {
        return new PaginatedResponse(this);
    }
}

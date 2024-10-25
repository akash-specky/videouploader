package com.example.videouploader.model;



import java.util.List;


public class PaginatedResponse {

    private List<VideoDetails> videos;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public PaginatedResponse(List<VideoDetails> content, long totalElements, int totalPages, Integer pageNo, Integer size) {
            this.videos = content;
            this.totalElements = totalElements;
            this.totalPages=totalPages;
            this.currentPage = pageNo;
            this.pageSize = size;
    }

    public List<VideoDetails> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDetails> videos) {
        this.videos = videos;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

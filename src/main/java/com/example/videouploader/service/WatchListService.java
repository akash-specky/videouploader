package com.example.videouploader.service;


import com.example.videouploader.model.WatchList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WatchListService {

     WatchList addToWatchList(String userId, String videoId) ;

    public List<WatchList> getWatchList(String userId);
}

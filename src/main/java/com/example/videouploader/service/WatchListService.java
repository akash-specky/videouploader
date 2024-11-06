package com.example.videouploader.service;


import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.dto.LikeDTO;
import com.example.videouploader.model.WatchList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WatchListService {

     WatchList addToWatchList(LikeDTO dto) throws VideoException;

    public List<WatchList> getWatchList(String userId);
}

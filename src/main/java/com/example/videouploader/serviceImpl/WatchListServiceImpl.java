package com.example.videouploader.serviceImpl;

import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.dto.LikeDTO;
import com.example.videouploader.model.WatchList;
import com.example.videouploader.repository.WatchListRepository;
import com.example.videouploader.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private WatchListRepository watchListRepository;


    @Override
    public WatchList addToWatchList(LikeDTO dto) throws VideoException {
        if (watchListRepository.existsByUserIdAndVideoId(dto.getUserId(), dto.getVideoId())) {
            throw new VideoException("Video is already in the watchlist for this user.");
        }
        WatchList watchList = new WatchList();
        watchList.setUserId(dto.getUserId());
        watchList.setVideoId(dto.getVideoId());
        return watchListRepository.save(watchList);
    }

    @Override
    public List<WatchList> getWatchList(String userId) {
        return watchListRepository.findByUserId(userId);
    }
}

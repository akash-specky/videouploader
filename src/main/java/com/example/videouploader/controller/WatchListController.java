package com.example.videouploader.controller;


import com.example.videouploader.Exception.VideoException;
import com.example.videouploader.model.WatchList;
import com.example.videouploader.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;

    @PostMapping
    public ResponseEntity<WatchList> addToWatchList(@RequestParam String userId, @RequestParam String videoId) throws VideoException {
        WatchList watchList = watchListService.addToWatchList(userId, videoId);
        return ResponseEntity.ok(watchList);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WatchList>> getWatchList(@PathVariable String userId) {
        List<WatchList> watchList = watchListService.getWatchList(userId);
        return ResponseEntity.ok(watchList);
    }
}

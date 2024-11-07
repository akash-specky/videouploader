package com.example.videouploader.controller;

import com.example.videouploader.repository.VideoDetailsRepository;
import com.example.videouploader.service.VideoDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/filter")
public class AdvanceFilterController {

    @Autowired
    private VideoDetailsRepository videoDetailsRepository;

    @Autowired
    private VideoDetailsService videoDetailsService;

    @GetMapping("/advanceFilter")
    public ResponseEntity<Map<String, List<String>>> getFilterOptions() {
        Map<String, List<String>> filterOptions = videoDetailsService.getFilterOptions();
        return ResponseEntity.ok(filterOptions);
    }
}

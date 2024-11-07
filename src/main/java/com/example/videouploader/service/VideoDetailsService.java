package com.example.videouploader.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface VideoDetailsService {
    Map<String, List<String>> getFilterOptions();
}

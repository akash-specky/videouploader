package com.example.videouploader.service;


import com.example.videouploader.dtos.FilterParams;
import org.springframework.stereotype.Service;

@Service
public interface NLPService {
    public FilterParams processQuery(String query);
}

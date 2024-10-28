package com.example.videouploader.service;


import com.example.videouploader.dtos.SearchDTO;
import org.springframework.stereotype.Service;

@Service
public interface NLPService {
    public SearchDTO processQuery(String query);
}

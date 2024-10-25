package com.example.videouploader.serviceImpl;


import com.example.videouploader.dtos.FilterParams;
import com.example.videouploader.service.NLPService;
import org.springframework.stereotype.Service;

@Service
public class NLPServiceImpl implements NLPService {
    @Override
    public FilterParams processQuery(String query) {
        FilterParams filterParams = new FilterParams();

        if (query.contains("short") || query.contains("less than 5 minutes")) {
            filterParams.setDuration("short");
        }
        if (query.contains("mp4")) {
            filterParams.setFormat("mp4");
        }
        if (query.contains("today") || query.contains("this week")) {
            filterParams.setUploadTime("today");
        }


        return filterParams;
    }
}

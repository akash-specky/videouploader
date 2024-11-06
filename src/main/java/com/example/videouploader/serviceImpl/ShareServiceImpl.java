package com.example.videouploader.serviceImpl;

import com.example.videouploader.service.ShareService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class ShareServiceImpl implements ShareService {


    private final Map<String, String> tinyUrlMap = new HashMap<>();
    private final String domain = "http://short.ly/";


    @Override
    public String generateTinyUrl(String videoId) {
        String tinyUrl = domain + getRandomString();
        tinyUrlMap.put(tinyUrl, videoId);
        return tinyUrl;
    }

    @Override
    public String getVideoId(String tinyUrl) {
        return tinyUrlMap.get(tinyUrl);
    }

    private String getRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}

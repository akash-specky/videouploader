package com.example.videouploader.service;


import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface VideoCombiningService {

    public File combineChunks(List<File> chunks, String videoId,String fileName) throws Exception ;
}

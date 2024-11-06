package com.example.videouploader.serviceImpl;


import com.example.videouploader.dtos.SearchDTO;
import com.example.videouploader.service.NLPService;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.stereotype.Service;

@Service
public class NLPServiceImpl implements NLPService {
    @Override
    public SearchDTO processQuery(String query) {
        SearchDTO searchDTO = new SearchDTO();
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

        String[] tokens = tokenizer.tokenize(query);

        for (String token : tokens) {
            if (token.matches("\\d+min")) {
                searchDTO.setDuration(token);
            } else if (token.matches("MP4|AVI|MKV")) {
                searchDTO.setFormat(token);
            } else if (token.matches("\\d{4}-\\d{2}-\\d{2}")) {
                searchDTO.setUploadTime(token);
            }
        }

        return searchDTO;
    }
}

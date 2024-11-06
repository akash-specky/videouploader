package com.example.videouploader.service;


import com.example.videouploader.dto.CommentDTO;
import com.example.videouploader.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    public Comment addComment(CommentDTO commentDTO);

    public List<Comment> getComments(String videoId);
}

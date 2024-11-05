package com.example.videouploader.serviceImpl;


import com.example.videouploader.model.Comment;
import com.example.videouploader.repository.CommentRepository;
import com.example.videouploader.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public Comment addComment(String userId, String videoId, String content) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setVideoId(videoId);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(String videoId) {
        return commentRepository.findByVideoIdOrderByCreatedAtDesc(videoId);

    }
}

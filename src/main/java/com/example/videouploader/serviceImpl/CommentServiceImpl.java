package com.example.videouploader.serviceImpl;


import com.example.videouploader.dto.CommentDTO;
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
    public Comment addComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setUserId(comment.getUserId());
        comment.setVideoId(commentDTO.getVideoId());
        comment.setContent(commentDTO.getContent());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(String videoId) {
        return commentRepository.findByVideoIdOrderByCreatedAtDesc(videoId);

    }
}

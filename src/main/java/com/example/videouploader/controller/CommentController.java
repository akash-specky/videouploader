package com.example.videouploader.controller;

import com.example.videouploader.dto.CommentDTO;
import com.example.videouploader.model.Comment;
import com.example.videouploader.service.CommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class CommentController {


    @Autowired
    private CommentService commentService;

    @PostMapping("/{videoId}/comments")
    public ResponseEntity<Comment> addComment(@RequestBody CommentDTO commentDTO) {
        Comment comment = commentService.addComment(commentDTO);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{videoId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String videoId) {
        List<Comment> comments = commentService.getComments(videoId);
        return ResponseEntity.ok(comments);
    }
}

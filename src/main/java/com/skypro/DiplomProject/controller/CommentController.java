package com.skypro.DiplomProject.controller;

import com.skypro.DiplomProject.dto.Comment;
import com.skypro.DiplomProject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ads")
public class CommentController {
    @Autowired
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("comments")
    public ResponseEntity<List<Comment>> getCommentsByAdsId(Integer id) {
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Integer adsId,@PathVariable String text) {
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("{adsId}/comments/{commentId}")
    public ResponseEntity<List<Comment>> deleteCommentsByAdsId(Integer adsId, Integer commentId) {
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PutMapping("{adsId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer adsId,@PathVariable String text) {
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}

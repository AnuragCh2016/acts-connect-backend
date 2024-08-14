package com.connect.acts.ActsConnectBackend.controller;

import com.connect.acts.ActsConnectBackend.dto.CommentRequest;
import com.connect.acts.ActsConnectBackend.dto.CommentResponse;
import com.connect.acts.ActsConnectBackend.model.Comment;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.service.CommentService;
import com.connect.acts.ActsConnectBackend.service.PostService;
import com.connect.acts.ActsConnectBackend.service.UserService;
import com.connect.acts.ActsConnectBackend.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;
    private final JwtUtil jwtUtil;

    public CommentController(CommentService commentService, UserService userService, PostService postService, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<CommentResponse> createComment(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CommentRequest commentRequest
    ) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        Post post = postService.findById(commentRequest.getPostId());

        if (user == null || post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Comment comment = commentService.createComment(user, post, commentRequest);
        CommentResponse response = new CommentResponse(comment.getId(), comment.getText(), comment.getCreatedAt());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private String extractEmailFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // Use JwtUtil or similar to extract the email
        return jwtUtil.extractEmail(token); // Replace with actual extraction logic
    }
}

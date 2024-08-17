package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.CommentRequest;
import com.connect.acts.ActsConnectBackend.dto.CommentResponse;
import com.connect.acts.ActsConnectBackend.model.Comment;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.repo.CommentRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    public CommentService(final CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public CommentResponse createComment(final User user, final Post post, final CommentRequest commentRequest) {
        final Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setPost(post);
        comment.setUser(user);
//    comment.setUpdatedAt(LocalDateTime.now());
        this.commentRepo.save(comment);

        return new CommentResponse(comment.getId(), comment.getText(), comment.getCreatedAt());
    }

    public Comment findById(final UUID commentId) {
        final Optional<Comment> commentOptional = this.commentRepo.findById(commentId);
        return commentOptional.orElse(null); // Return the comment if found, otherwise null
    }

    public CommentResponse editComment(final Comment comment, @Valid final CommentRequest commentRequest) {
        comment.setText(commentRequest.getText());
        comment.setUpdatedAt(LocalDateTime.now()); // Set the updated time
        this.commentRepo.save(comment); // Save the updated comment

        return new CommentResponse(comment.getId(), comment.getText(), comment.getUpdatedAt());
    }

    public void deleteComment(final Comment comment) {
        this.commentRepo.delete(comment);
    }
}



package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.CommentRequest;
import com.connect.acts.ActsConnectBackend.model.Comment;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.repo.CommentRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

  private final CommentRepo commentRepo;

  public CommentService(CommentRepo commentRepo) {
    this.commentRepo = commentRepo;
  }

  public Comment createComment(User user, Post post, CommentRequest commentRequest) {
    Comment comment = new Comment();
    comment.setText(commentRequest.getText());
    comment.setPost(post);
    comment.setUser(user);
    comment.setUpdatedAt(LocalDateTime.now());
    return commentRepo.save(comment);
  }
}

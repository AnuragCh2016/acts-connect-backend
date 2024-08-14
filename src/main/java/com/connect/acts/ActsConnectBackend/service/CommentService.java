package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.repo.CommentRepo;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
  private final CommentRepo commentRepo;

  public CommentService(CommentRepo commentRepo) {
    this.commentRepo = commentRepo;
  }
}

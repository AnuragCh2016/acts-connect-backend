package com.connect.acts.ActsConnectBackend.repository;

import com.connect.acts.ActsConnectBackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}

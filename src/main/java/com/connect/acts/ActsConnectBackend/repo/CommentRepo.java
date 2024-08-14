package com.connect.acts.ActsConnectBackend.repo;

import com.connect.acts.ActsConnectBackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepo extends JpaRepository<Comment, UUID> {
}

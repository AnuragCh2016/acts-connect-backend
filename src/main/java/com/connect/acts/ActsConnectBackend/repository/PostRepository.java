package com.connect.acts.ActsConnectBackend.repository;

import com.connect.acts.ActsConnectBackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}

package com.connect.acts.ActsConnectBackend.repo;

import com.connect.acts.ActsConnectBackend.dto.PostDTO;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PostRepo extends JpaRepository<Post, UUID> {
  @Query("SELECT new com.connect.acts.ActsConnectBackend.dto.PostDTO(" +
    "p.id, p.title, p.content, p.isDummy, p.createdAt, p.updatedAt, " +
    "p.user.id, p.user.name) " +
    "FROM Post p WHERE p.isDummy = true ORDER BY p.createdAt DESC")
  List<PostDTO> findDummyPosts();

  @Query("SELECT new com.connect.acts.ActsConnectBackend.dto.PostDTO(" +
    "p.id, p.title, p.content, p.isDummy, p.createdAt, p.updatedAt, " +
    "p.user.id, p.user.name) " +
    "FROM Post p WHERE p.user IN :followingUsers ORDER BY p.createdAt DESC")
  List<PostDTO> findRecentPosts(@Param("followingUsers") Set<User> followingUsers);

}

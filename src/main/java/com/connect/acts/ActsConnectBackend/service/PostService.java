package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.PostDTO;
import com.connect.acts.ActsConnectBackend.dto.PostRequestDTO;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.repo.PostRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
  private final PostRepo postRepo;

  private final UserService userService;

  public PostService(PostRepo postRepo, UserService userService) {
    this.postRepo = postRepo;
    this.userService = userService;
  }

  public List<PostDTO> getPosts(User user) {
    Set<User> followingUsers = userService.getFollowing(user); // Get the users the logged-in user is following
    long followingCount = followingUsers.size();

    List<PostDTO> posts;
    if (followingCount < 3) {
      List<PostDTO> dummyPosts = postRepo.findDummyPosts();
      List<PostDTO> recentPosts = postRepo.findRecentPosts(followingUsers);
      posts = new ArrayList<>();
      posts.addAll(dummyPosts);
      posts.addAll(recentPosts);

      // sort posts in descending oder
      posts = posts.stream()
        .sorted(Comparator.comparing(PostDTO::getCreatedAt).reversed())
        .collect(Collectors.toList());
    } else {
      posts = postRepo.findRecentPosts(followingUsers);
    }

    return posts;
  }

  public PostDTO createPost(User user, PostRequestDTO postRequestDTO) {
    Post post = new Post();
    post.setTitle(postRequestDTO.getTitle());
    post.setContent(postRequestDTO.getContent());
    post.setUser(user);
    post.setDummy(false);

    postRepo.save(post);

    PostDTO postDTO = new PostDTO(post.getId(), post.getTitle(), post.getContent(), post.isDummy(), post.getCreatedAt(), post.getUpdatedAt(), post.getUser().getId(), post.getUser().getName());
    return postDTO;
  }
}

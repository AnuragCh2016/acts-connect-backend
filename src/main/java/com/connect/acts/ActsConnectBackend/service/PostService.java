package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.PostDTO;
import com.connect.acts.ActsConnectBackend.dto.PostRequestDTO;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.repo.PostRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepo postRepo;

    private final UserService userService;

    public PostService(final PostRepo postRepo, final UserService userService) {
        this.postRepo = postRepo;
        this.userService = userService;
    }

    public List<PostDTO> getPosts(final User user) {
        final Set<User> followingUsers = this.userService.getFollowing(user); // Get the users the logged-in user is following
        final long followingCount = followingUsers.size();

        List<PostDTO> posts;
        if (3 > followingCount) {
            final List<PostDTO> dummyPosts = this.postRepo.findDummyPosts();
            final List<PostDTO> recentPosts = this.postRepo.findRecentPosts(followingUsers);
            posts = new ArrayList<>();
            posts.addAll(dummyPosts);
            posts.addAll(recentPosts);

            // sort posts in descending oder
            posts = posts.stream()
                    .sorted(Comparator.comparing(PostDTO::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        } else {
            posts = this.postRepo.findRecentPosts(followingUsers);
        }

        return posts;
    }

    public PostDTO createPost(final User user, final PostRequestDTO postRequestDTO) {
        final Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        post.setUser(user);
        post.setDummy(false);

        this.postRepo.save(post);

        return new PostDTO(post.getId(), post.getTitle(), post.getContent(), post.isDummy(), post.getCreatedAt(), post.getUpdatedAt(), post.getUser().getId(), post.getUser().getName());
    }

    public Post findById(final UUID postId) {
        final Optional<Post> post = this.postRepo.findById(postId);
        return post.orElse(null); // or throw an exception if preferred
    }

    public PostDTO editPost(final User user, final UUID postId, final PostRequestDTO postRequestDTO) {
        final Optional<Post> postOptional = this.postRepo.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getUser().equals(user)) {
                post.setTitle(postRequestDTO.getTitle());
                post.setContent(postRequestDTO.getContent());
                post = this.postRepo.save(post);
                return new PostDTO(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(), post.getUser().getId());
            }
        }
        return null;
    }

    public boolean deletePost(final User user, final UUID postId) {
        final Optional<Post> postOptional = this.postRepo.findById(postId);

        if (postOptional.isPresent()) {
            final Post post = postOptional.get();
            if (post.getUser().equals(user)) {
                this.postRepo.delete(post);
                return true;
            }
        }
        return false;
    }
}

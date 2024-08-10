package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.PostDTO;
import com.connect.acts.ActsConnectBackend.exception.ResourceNotFoundException;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.repository.PostRepository;
import com.connect.acts.ActsConnectBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostDTO createPost(PostDTO postDto) {
        // Retrieve the user who is creating the post
        Optional<User> userOptional = userRepository.findById(postDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + postDto.getUserId());
        }
        User user = userOptional.get();

        // Create the post entity from the DTO
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUser(user);
        post.setLikedByUsers(postDto.getLikedByUsers().stream()
                .map(likedUserId -> userRepository.findById(likedUserId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + likedUserId)))
                .collect(Collectors.toSet()));

        // Save the post
        Post savedPost = postRepository.save(post);

        // Return the DTO with the ID
        postDto.setId(savedPost.getId());
        postDto.setCreatedAt(savedPost.getCreatedAt());
        postDto.setUpdatedAt(savedPost.getUpdatedAt());
        return postDto;
    }

    public PostDTO updatePost(UUID postId, PostDTO postDto) {
        // Find the post by ID
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Post not found with ID: " + postId);
        }
        Post post = postOptional.get();

        // Update the post entity with the new details
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setLikedByUsers(postDto.getLikedByUsers().stream()
                .map(likedUserId -> userRepository.findById(likedUserId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + likedUserId)))
                .collect(Collectors.toSet()));

        // Save the updated post
        Post updatedPost = postRepository.save(post);

        // Return the DTO with the updated timestamp
        postDto.setCreatedAt(updatedPost.getCreatedAt());
        postDto.setUpdatedAt(updatedPost.getUpdatedAt());
        return postDto;
    }

    public void deletePost(UUID postId) {
        // Find the post by ID
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Post not found with ID: " + postId);
        }

        // Delete the post
        postRepository.delete(postOptional.get());
    }

    public PostDTO getPostById(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));
        PostDTO postDto = new PostDTO();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setUserId(post.getUser().getId());
        postDto.setLikedByUsers(post.getLikedByUsers().stream().map(User::getId).collect(Collectors.toSet()));
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setUpdatedAt(post.getUpdatedAt());
        return postDto;
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream().map(post -> {
            PostDTO postDto = new PostDTO();
            postDto.setId(post.getId());
            postDto.setTitle(post.getTitle());
            postDto.setContent(post.getContent());
            postDto.setUserId(post.getUser().getId());
            postDto.setLikedByUsers(post.getLikedByUsers().stream().map(User::getId).collect(Collectors.toSet()));
            postDto.setCreatedAt(post.getCreatedAt());
            postDto.setUpdatedAt(post.getUpdatedAt());
            return postDto;
        }).collect(Collectors.toList());
    }
}

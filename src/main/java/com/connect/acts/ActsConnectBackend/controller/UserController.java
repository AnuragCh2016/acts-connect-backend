package com.connect.acts.ActsConnectBackend.controller;

import com.connect.acts.ActsConnectBackend.dto.PostDTO;
import com.connect.acts.ActsConnectBackend.dto.PostResponse;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.service.PostService;
import com.connect.acts.ActsConnectBackend.service.UserService;
import com.connect.acts.ActsConnectBackend.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final JwtUtil jwtUtil;
  private final UserService userService;

  private final PostService postService;

  public UserController(JwtUtil jwtUtil, UserService userService, PostService postService) {
    this.jwtUtil = jwtUtil;
    this.userService = userService;
    this.postService = postService;
  }

  @GetMapping("/posts")
  public ResponseEntity<PostResponse> getPosts(@RequestHeader("Authorization") String token) {
    System.out.println("Original token: "+token);
    // if token is Bearer type remove it
    if (token.startsWith("Bearer ")) {
      token = token.substring(7);
    }

    System.out.println("Token after removing Bearer: "+token);

    // extract email
    String email = jwtUtil.extractEmail(token);

    System.out.println("Email extracted from token: "+email);

    // get User
    User user = userService.findByEmail(email);

    // fetch posts
    List<PostDTO> posts = postService.getPosts(user);

    // create response
    PostResponse postResponse = new PostResponse(200, posts);

    // return response
    return ResponseEntity.ok(postResponse);
  }

  @PostMapping("/follow/{userId}")
  public ResponseEntity<String> followUser(@RequestHeader("Authorization") String token, @PathVariable UUID userId) {
    // Extract the email from the token
    String email = extractEmailFromToken(token);

    // Get the logged-in user
    User loggedInUser = userService.findByEmail(email);

    // Find the user to follow
    User userToFollow = userService.findById(userId);

    if (userToFollow == null) {
      return ResponseEntity.badRequest().body("User not found.");
    }

    // Perform the follow action
    userService.followUser(loggedInUser, userToFollow);

    return ResponseEntity.ok("Successfully followed the user.");
  }

  @PostMapping("/unfollow/{userId}")
  public ResponseEntity<String> unfollowUser(@RequestHeader("Authorization") String token, @PathVariable UUID userId) {
    // Extract the email from the token
    String email = extractEmailFromToken(token);

    // Get the logged-in user
    User loggedInUser = userService.findByEmail(email);

    // Find the user to unfollow
    User userToUnfollow = userService.findById(userId);

    if (userToUnfollow == null) {
      return ResponseEntity.badRequest().body("User not found.");
    }

    // Perform the unfollow action
    userService.unfollowUser(loggedInUser, userToUnfollow);

    return ResponseEntity.ok("Successfully unfollowed the user.");
  }

  private String extractEmailFromToken(String token) {
    if (token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    return jwtUtil.extractEmail(token);
  }
}

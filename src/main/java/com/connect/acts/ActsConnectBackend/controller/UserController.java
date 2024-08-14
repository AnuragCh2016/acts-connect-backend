package com.connect.acts.ActsConnectBackend.controller;

import com.connect.acts.ActsConnectBackend.dto.PostDTO;
import com.connect.acts.ActsConnectBackend.dto.PostResponse;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.service.PostService;
import com.connect.acts.ActsConnectBackend.service.UserService;
import com.connect.acts.ActsConnectBackend.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

}

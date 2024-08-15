package com.connect.acts.ActsConnectBackend.controller;

import com.connect.acts.ActsConnectBackend.dto.*;
import com.connect.acts.ActsConnectBackend.dto.CommentRequest;
import com.connect.acts.ActsConnectBackend.dto.CommentResponse;
import com.connect.acts.ActsConnectBackend.dto.PostDTO;
import com.connect.acts.ActsConnectBackend.dto.PostResponse;
import com.connect.acts.ActsConnectBackend.model.Comment;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.service.CommentService;
import com.connect.acts.ActsConnectBackend.service.PostService;
import com.connect.acts.ActsConnectBackend.service.UserService;
import com.connect.acts.ActsConnectBackend.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JwtUtil jwtUtil;

    private final UserService userService;

    private final PostService postService;

    private final CommentService commentService;


    public UserController(JwtUtil jwtUtil, UserService userService, PostService postService, CommentService commentService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    // for posts

    //get posts
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getPosts(@RequestHeader("Authorization") String token) {
        // if token is Bearer type remove it
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // extract email
        String email = jwtUtil.extractEmail(token);

        // get User
        User user = userService.findByEmail(email);

        // fetch posts
        List<PostDTO> posts = postService.getPosts(user);

        // create response
        PostResponse postResponse = new PostResponse(200, posts);

        // return response
        return ResponseEntity.ok(postResponse);
    }

    // create a post
    @PostMapping("/post/create")
    public ResponseEntity<PostDTO> createPost(@RequestHeader("Authorization") String token, @RequestBody PostRequestDTO postRequestDTO) {
        // if token is Bearer type remove it
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // extract email
        String email = jwtUtil.extractEmail(token);

        // get User
        User user = userService.findByEmail(email);

        // create post
        PostDTO post = postService.createPost(user, postRequestDTO);

        // return response
        return ResponseEntity.ok(post);
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
        } else if (userToFollow == loggedInUser) {
            return ResponseEntity.badRequest().body("You cannot follow yourself.");
        } else if (loggedInUser.getFollowing().contains(userToFollow)) {
            return ResponseEntity.badRequest().body("You are already following this user.");
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

        // Check if the user is part of the following list
        if (!loggedInUser.getFollowing().contains(userToUnfollow)) {
            return ResponseEntity.badRequest().body("User is not in your following list.");
        }

        // Perform the unfollow action
        userService.unfollowUser(loggedInUser, userToUnfollow);

        return ResponseEntity.ok("Successfully unfollowed the user.");
    }


    @PostMapping("/comment/create")
    public ResponseEntity<CommentResponse> createComment(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CommentRequest commentRequest
    ) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        Post post = postService.findById(commentRequest.getPostId());
        if (user == null || post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CommentResponse commentResponse = commentService.createComment(user, post, commentRequest);
//    CommentResponse response = new CommentResponse(comment.getId(), comment.getText(), comment.getCreatedAt());

        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    // Edit a comment
    @PutMapping("/comment/edit/{commentId}")
    public ResponseEntity<CommentResponse> editComment(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID commentId,
            @RequestBody @Valid CommentRequest commentRequest
    ) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        Comment comment = commentService.findById(commentId);
        if (comment == null || !comment.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CommentResponse updatedComment = commentService.editComment(comment, commentRequest);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    // Delete a comment
    @DeleteMapping("/comment/delete/{commentId}")
    public ResponseEntity<String> deleteComment(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID commentId
    ) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        Comment comment = commentService.findById(commentId);
        if (comment == null || !comment.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        commentService.deleteComment(comment);
        return ResponseEntity.ok("Comment deleted successfully.");
    }


    private String extractEmailFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractEmail(token);
    }


    @PostMapping("/search")
    public ResponseEntity<List<UUID>> searchUsers(@RequestHeader("Authorization") String token, @RequestBody UserSearchRequest searchRequest) {

        // Extract the email from the token
        String email = extractEmailFromToken(token);

        // Check logged in user
        User loggedInUser = userService.findByEmail(email);

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<User> users = userService.searchUsers(searchRequest);

        List<UUID> userIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        return new ResponseEntity<>(userIds, HttpStatus.OK);
    }

    // frontend can make a call to this endpoint to get the user details by id for each id
    // gotten from /search
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        User user = userService.findById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCompany(),
                user.getCourseType(),
                user.getBatchYear()
        );

        return ResponseEntity.ok(userResponseDTO);
    }
}

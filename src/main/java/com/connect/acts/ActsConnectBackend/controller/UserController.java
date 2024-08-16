package com.connect.acts.ActsConnectBackend.controller;

import com.connect.acts.ActsConnectBackend.dto.*;
import com.connect.acts.ActsConnectBackend.model.Comment;
import com.connect.acts.ActsConnectBackend.model.Post;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.service.CommentService;
import com.connect.acts.ActsConnectBackend.service.JobService;
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
    private final JobService jobService;

    public UserController(JwtUtil jwtUtil, UserService userService, PostService postService, CommentService commentService, JobService jobService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.jobService = jobService;
    }

    // Create a job posting
    @PostMapping("/job/create")
    public ResponseEntity<JobDTO> createJob(@RequestHeader("Authorization") String token, @RequestBody @Valid JobRequestDTO jobRequestDTO) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        JobDTO job = jobService.createJob(user, jobRequestDTO);
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    // Get all job postings
    @GetMapping("/jobs")
    public ResponseEntity<List<JobDTO>> getJobs(@RequestHeader("Authorization") String token) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<JobDTO> jobs = jobService.getJobs();
        return ResponseEntity.ok(jobs);
    }

    // Update a job posting
    @PutMapping("/job/update/{jobId}")
    public ResponseEntity<JobDTO> updateJob(@RequestHeader("Authorization") String token, @PathVariable UUID jobId, @RequestBody @Valid JobRequestDTO jobRequestDTO) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        JobDTO updatedJob = jobService.updateJob(user, jobId, jobRequestDTO);
        if (updatedJob == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(updatedJob);
    }

    // Delete a job posting
    @DeleteMapping("/job/delete/{jobId}")
    public ResponseEntity<Void> deleteJob(@RequestHeader("Authorization") String token, @PathVariable UUID jobId) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean isDeleted = jobService.deleteJob(user, jobId);
        if (!isDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Apply for Jobs
    @PostMapping("/jobs/apply")
    public ResponseEntity<String> applyForJob(@RequestHeader("Authorization") String token, @Valid JobApplicationDTO jobApplicationDTO) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean applied = jobService.applyForJob(user, jobApplicationDTO);
        if (!applied) {
            return new ResponseEntity<>("Job not found or application failed.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Application Successful.", HttpStatus.OK);

    }

    // Get posts
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getPosts(@RequestHeader("Authorization") String token) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        List<PostDTO> posts = postService.getPosts(user);
        PostResponse postResponse = new PostResponse(200, posts);
        return ResponseEntity.ok(postResponse);
    }

    // Create a post
    @PostMapping("/post/create")
    public ResponseEntity<PostDTO> createPost(@RequestHeader("Authorization") String token, @RequestBody PostRequestDTO postRequestDTO) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        PostDTO post = postService.createPost(user, postRequestDTO);
        return ResponseEntity.ok(post);
    }

    // Follow a user
    @PostMapping("/follow/{userId}")
    public ResponseEntity<String> followUser(@RequestHeader("Authorization") String token, @PathVariable UUID userId) {
        String email = extractEmailFromToken(token);
        User loggedInUser = userService.findByEmail(email);
        User userToFollow = userService.findById(userId);

        if (userToFollow == null) {
            return ResponseEntity.badRequest().body("User not found.");
        } else if (userToFollow.equals(loggedInUser)) {
            return ResponseEntity.badRequest().body("You cannot follow yourself.");
        } else if (loggedInUser.getFollowing().contains(userToFollow)) {
            return ResponseEntity.badRequest().body("You are already following this user.");
        }

        userService.followUser(loggedInUser, userToFollow);
        return ResponseEntity.ok("Successfully followed the user.");
    }

    // Unfollow a user
    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<String> unfollowUser(@RequestHeader("Authorization") String token, @PathVariable UUID userId) {
        String email = extractEmailFromToken(token);
        User loggedInUser = userService.findByEmail(email);
        User userToUnfollow = userService.findById(userId);

        if (userToUnfollow == null) {
            return ResponseEntity.badRequest().body("User not found.");
        } else if (!loggedInUser.getFollowing().contains(userToUnfollow)) {
            return ResponseEntity.badRequest().body("User is not in your following list.");
        }

        userService.unfollowUser(loggedInUser, userToUnfollow);
        return ResponseEntity.ok("Successfully unfollowed the user.");
    }

    // Create a comment
    @PostMapping("/comment/create")
    public ResponseEntity<CommentResponse> createComment(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CommentRequest commentRequest) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        Post post = postService.findById(commentRequest.getPostId());

        if (user == null || post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CommentResponse commentResponse = commentService.createComment(user, post, commentRequest);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    // Edit a comment
    @PutMapping("/comment/edit/{postId}")
    public ResponseEntity<CommentResponse> editComment(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID postId,
            @RequestBody @Valid CommentRequest commentRequest) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        Post post = postService.findById(postId);

        if (post == null || !post.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Comment comment = post.getComments().stream()
                .filter(c -> c.getId().equals(commentRequest.getCommentId()))
                .findFirst()
                .orElse(null);

        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CommentResponse updatedComment = commentService.editComment(comment, commentRequest);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    // Delete a comment
    @DeleteMapping("/comment/delete/{postId}")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID postId,
            @RequestBody @Valid CommentRequest commentRequest) {
        String email = extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        Post post = postService.findById(postId);

        if (post == null || !post.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Comment comment = post.getComments().stream()
                .filter(c -> c.getId().equals(commentRequest.getCommentId()))
                .findFirst()
                .orElse(null);

        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        commentService.deleteComment(comment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search for users
    @PostMapping("/search")
    public ResponseEntity<List<UUID>> searchUsers(@RequestHeader("Authorization") String token, @RequestBody UserSearchRequest searchRequest) {
        String email = extractEmailFromToken(token);
        User loggedInUser = userService.findByEmail(email);

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<User> users = userService.searchUsers(searchRequest);
        List<UUID> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        return new ResponseEntity<>(userIds, HttpStatus.OK);
    }

    // Get user by ID
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

    private String extractEmailFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractEmail(token);
    }
}

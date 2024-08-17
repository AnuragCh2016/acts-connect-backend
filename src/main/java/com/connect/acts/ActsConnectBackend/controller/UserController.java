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

    public UserController(final JwtUtil jwtUtil, final UserService userService, final PostService postService, final CommentService commentService, final JobService jobService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.jobService = jobService;
    }

    // Create a job posting
    @PostMapping("/job/create")
    public ResponseEntity<JobDTO> createJob(@RequestHeader("Authorization") final String token, @RequestBody @Valid final JobRequestDTO jobRequestDTO) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);

        if (null == user) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final JobDTO job = this.jobService.createJob(user, jobRequestDTO);
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    // Get all job postings
    @GetMapping("/jobs")
    public ResponseEntity<List<JobDTO>> getJobs(@RequestHeader("Authorization") final String token) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);

        if (null == user) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final List<JobDTO> jobs = this.jobService.getJobs();
        return ResponseEntity.ok(jobs);
    }

    // Update a job posting
    @PutMapping("/job/update/{jobId}")
    public ResponseEntity<JobDTO> updateJob(@RequestHeader("Authorization") final String token, @PathVariable final UUID jobId, @RequestBody @Valid final JobRequestDTO jobRequestDTO) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);

        if (null == user) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final JobDTO updatedJob = this.jobService.updateJob(user, jobId, jobRequestDTO);
        if (null == updatedJob) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(updatedJob);
    }

    // Delete a job posting
    @DeleteMapping("/job/delete/{jobId}")
    public ResponseEntity<Void> deleteJob(@RequestHeader("Authorization") final String token, @PathVariable final UUID jobId) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);

        if (null == user) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final boolean isDeleted = this.jobService.deleteJob(user, jobId);
        if (!isDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Apply for Jobs
    @PostMapping("/jobs/apply")
    public ResponseEntity<String> applyForJob(@RequestHeader("Authorization") final String token, @Valid final JobApplicationDTO jobApplicationDTO) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);

        if (null == user) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final boolean applied = this.jobService.applyForJob(user, jobApplicationDTO);
        if (!applied) {
            return new ResponseEntity<>("Job not found or application failed.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Application Successful.", HttpStatus.OK);

    }

    // Get posts
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getPosts(@RequestHeader("Authorization") final String token) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);
        final List<PostDTO> posts = this.postService.getPosts(user);
        final PostResponse postResponse = new PostResponse(200, posts);
        return ResponseEntity.ok(postResponse);
    }

    // Create a post
    @PostMapping("/post/create")
    public ResponseEntity<PostDTO> createPost(@RequestHeader("Authorization") final String token, @RequestBody final PostRequestDTO postRequestDTO) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);
        final PostDTO post = this.postService.createPost(user, postRequestDTO);
        return ResponseEntity.ok(post);
    }

    // Follow a user
    @PostMapping("/follow/{userId}")
    public ResponseEntity<String> followUser(@RequestHeader("Authorization") final String token, @PathVariable final UUID userId) {
        final String email = this.extractEmailFromToken(token);
        final User loggedInUser = this.userService.findByEmail(email);
        final User userToFollow = this.userService.findById(userId);

        if (null == userToFollow) {
            return ResponseEntity.badRequest().body("User not found.");
        } else if (userToFollow.equals(loggedInUser)) {
            return ResponseEntity.badRequest().body("You cannot follow yourself.");
        } else if (loggedInUser.getFollowing().contains(userToFollow)) {
            return ResponseEntity.badRequest().body("You are already following this user.");
        }

        this.userService.followUser(loggedInUser, userToFollow);
        return ResponseEntity.ok("Successfully followed the user.");
    }

    // Unfollow a user
    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<String> unfollowUser(@RequestHeader("Authorization") final String token, @PathVariable final UUID userId) {
        final String email = this.extractEmailFromToken(token);
        final User loggedInUser = this.userService.findByEmail(email);
        final User userToUnfollow = this.userService.findById(userId);

        if (null == userToUnfollow) {
            return ResponseEntity.badRequest().body("User not found.");
        } else if (!loggedInUser.getFollowing().contains(userToUnfollow)) {
            return ResponseEntity.badRequest().body("User is not in your following list.");
        }

        this.userService.unfollowUser(loggedInUser, userToUnfollow);
        return ResponseEntity.ok("Successfully unfollowed the user.");
    }

    // Create a comment
    @PostMapping("/comment/create")
    public ResponseEntity<CommentResponse> createComment(
            @RequestHeader("Authorization") final String token,
            @RequestBody @Valid final CommentRequest commentRequest) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);
        final Post post = this.postService.findById(commentRequest.getPostId());

        if (null == user || null == post) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final CommentResponse commentResponse = this.commentService.createComment(user, post, commentRequest);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    // Edit a comment
    @PutMapping("/comment/edit/{postId}")
    public ResponseEntity<CommentResponse> editComment(
            @RequestHeader("Authorization") final String token,
            @PathVariable final UUID postId,
            @RequestBody @Valid final CommentRequest commentRequest) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);
        final Post post = this.postService.findById(postId);

        if (null == post || !post.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final Comment comment = post.getComments().stream()
                .filter(c -> c.getId().equals(commentRequest.getCommentId()))
                .findFirst()
                .orElse(null);

        if (null == comment) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final CommentResponse updatedComment = this.commentService.editComment(comment, commentRequest);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    // Delete a comment
    @DeleteMapping("/comment/delete/{postId}")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("Authorization") final String token,
            @PathVariable final UUID postId,
            @RequestBody @Valid final CommentRequest commentRequest) {
        final String email = this.extractEmailFromToken(token);
        final User user = this.userService.findByEmail(email);
        final Post post = this.postService.findById(postId);

        if (null == post || !post.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final Comment comment = post.getComments().stream()
                .filter(c -> c.getId().equals(commentRequest.getCommentId()))
                .findFirst()
                .orElse(null);

        if (null == comment) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.commentService.deleteComment(comment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search for users
    @GetMapping("/search")
    public ResponseEntity<List<UUID>> searchUsers(@RequestHeader("Authorization") final String token, @RequestBody final UserSearchRequest searchRequest) {
        final String email = this.extractEmailFromToken(token);
        final User loggedInUser = this.userService.findByEmail(email);

        if (null == loggedInUser) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        final List<User> users = this.userService.searchUsers(searchRequest);
        final List<UUID> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        return new ResponseEntity<>(userIds, HttpStatus.OK);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable final UUID id) {
        final User user = this.userService.findById(id);

        if (null == user) {
            return ResponseEntity.notFound().build();
        }

        final UserResponseDTO userResponseDTO = new UserResponseDTO(
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
        return this.jwtUtil.extractEmail(token);
    }
}

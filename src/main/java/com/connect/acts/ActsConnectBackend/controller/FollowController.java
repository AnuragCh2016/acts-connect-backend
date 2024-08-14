package com.connect.acts.ActsConnectBackend.controller;

import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    @Autowired
    private FollowerService followService;

    @PostMapping("/{userid}/follow")
    public ResponseEntity<?> followUser(@PathVariable UUID userid, @RequestParam UUID followerUserId) {
        followService.followUser(userid, followerUserId);
        return ResponseEntity.ok("Followed user successfully");
    }

    @PostMapping("/{userid}/unfollow")
    public ResponseEntity<?> unfollowUser(@PathVariable UUID userid, @RequestParam UUID followerUserId) {
        followService.unfollowUser(userid, followerUserId);
        return ResponseEntity.ok("Unfollowed user successfully");
    }

    @GetMapping("/{userid}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable UUID userid) {
        List<User> followers = followService.getFollowers(userid);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userid}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable UUID userid) {
        List<User> following = followService.getFollowing(userid);
        return ResponseEntity.ok(following);
    }
}



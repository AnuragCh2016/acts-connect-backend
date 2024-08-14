package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.model.UserFollower;
import com.connect.acts.ActsConnectBackend.repo.FollowerRepo;
import com.connect.acts.ActsConnectBackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FollowerService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FollowerRepo followerRepo;

    public void followUser(UUID userid, UUID followerUserId) {

        User user = userRepo.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User follower = userRepo.findById(followerUserId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        Optional<UserFollower> existingFollow = followerRepo.findByUserIdAndFollowerId(user.getId(), follower.getId());

        if (existingFollow.isPresent()) {
            throw new RuntimeException("Already following this user");
        }

        UserFollower userFollower = new UserFollower();
        userFollower.setUser(user);
        userFollower.setFollower(follower);
        userFollower.setFollowedAt(new Timestamp(System.currentTimeMillis()));

        followerRepo.save(userFollower);
    }

    public void unfollowUser(UUID userid, UUID followerUserId) {
        User user = userRepo.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User follower = userRepo.findById(followerUserId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));

        UserFollower userFollower = followerRepo.findByUserIdAndFollowerId(user.getId(), follower.getId())
                .orElseThrow(() -> new RuntimeException("Not following this user"));

        followerRepo.delete(userFollower);
    }

    public List<User> getFollowers(UUID userid) {
        User user = userRepo.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<UserFollower> followers = followerRepo.findById(userid);
        return followers.stream().map(UserFollower::getFollower).collect(Collectors.toList());
    }

    public List<User> getFollowing(UUID followerUserId) {
        User follower = userRepo.findById(followerUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<UserFollower> following = followerRepo.findById(follower.getId());
        return following.stream().map(UserFollower::getUser).collect(Collectors.toList());
    }
}

/*
*


*/
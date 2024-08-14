package com.connect.acts.ActsConnectBackend.repo;

import com.connect.acts.ActsConnectBackend.model.UserFollower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowerRepo extends JpaRepository<UserFollower,UUID> {
//    List<UserFollower> findByUserId(UUID userId);
//    List<UserFollower> findByFollowerId(UUID followerId);
    Optional<UserFollower> findByUserIdAndFollowerId(UUID userId, UUID followerId);
}

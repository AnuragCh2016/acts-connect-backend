package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.UserSearchRequest;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.model.UserType;
import com.connect.acts.ActsConnectBackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
  private final UserRepo userRepo;

  @Autowired
  public UserService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  public User findByEmail(String email) {
    return userRepo.findByEmail(email);
  }

  public UserType getUserTypeByEmail(String email) {
    User user = userRepo.findByEmail(email);
    return user!=null?user.getUserType():null;
  }

  public User findById(UUID userId) {
    return userRepo.findById(userId).orElse(null);
  }

  public long countFollowing(User user) {
    return user.getFollowing().size();
  }

  public Set<User> getFollowing(User user) {
    return user.getFollowing();
  }

  public void followUser(User follower, User user) {
    if (follower.equals(user)) {
      throw new IllegalArgumentException("Users cannot follow themselves.");
    }

    follower.getFollowing().add(user);
    user.getFollowers().add(follower);

    userRepo.save(follower);
    userRepo.save(user);
  }

  public void unfollowUser(User follower, User user) {
    follower.getFollowing().remove(user);
    user.getFollowers().remove(follower);

    userRepo.save(follower);
    userRepo.save(user);
  }

  public List<User> searchUsers(UserSearchRequest searchRequest) {
    return userRepo.searchUsers(
      searchRequest.getName(),
      searchRequest.getMinBatchYear(),
      searchRequest.getMaxBatchYear(),
      searchRequest.getCompany(),
      searchRequest.getCourseType()
    );
  }
}


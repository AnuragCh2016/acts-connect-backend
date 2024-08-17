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
  public UserService(final UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  public User findByEmail(final String email) {
    return this.userRepo.findByEmail(email);
  }

  public UserType getUserTypeByEmail(final String email) {
    final User user = this.userRepo.findByEmail(email);
    return null != user ?user.getUserType():null;
  }

  public User findById(final UUID userId) {
    return this.userRepo.findById(userId).orElse(null);
  }

  public long countFollowing(final User user) {
    return user.getFollowing().size();
  }

  public Set<User> getFollowing(final User user) {
    return user.getFollowing();
  }

  public void followUser(final User follower, final User user) {
    if (follower.equals(user)) {
      throw new IllegalArgumentException("Users cannot follow themselves.");
    }

    follower.getFollowing().add(user);
    user.getFollowers().add(follower);

      this.userRepo.save(follower);
      this.userRepo.save(user);
  }

  public void unfollowUser(final User follower, final User user) {
    follower.getFollowing().remove(user);
    user.getFollowers().remove(follower);

      this.userRepo.save(follower);
      this.userRepo.save(user);
  }

  public List<User> searchUsers(final UserSearchRequest searchRequest) {
    return this.userRepo.searchUsers(
      searchRequest.getName(),
      searchRequest.getMinBatchYear(),
      searchRequest.getMaxBatchYear(),
      searchRequest.getCompany(),
      searchRequest.getCourseType()
    );
  }
}
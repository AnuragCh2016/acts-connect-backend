package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.model.UserType;
import com.connect.acts.ActsConnectBackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

package com.connect.acts.ActsConnectBackend.repo;

import com.connect.acts.ActsConnectBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
  User findByEmail(String email);
}

package com.connect.acts.ActsConnectBackend.repo;

import com.connect.acts.ActsConnectBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthRepo extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);
}

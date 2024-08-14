package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.LoginRequest;
import com.connect.acts.ActsConnectBackend.dto.RegisterRequest;
import com.connect.acts.ActsConnectBackend.dto.UserResponse;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.repo.AuthRepo;
import com.connect.acts.ActsConnectBackend.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final AuthRepo authRepo;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(AuthRepo authRepo) {
    this.authRepo = authRepo;
    this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    this.jwtUtil = new JwtUtil();
  }

  public UserResponse registerUser(RegisterRequest registerRequest) {
    // register user
    if(authRepo.findByEmail(registerRequest.getEmail()).isPresent()) {
      throw new RuntimeException("Email already exists");
    }
    User user = new User();
    user.setEmail(registerRequest.getEmail());
    user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
    user.setUserType(registerRequest.getUserType());
    user.setName(registerRequest.getName());
    user.setBatchYear(registerRequest.getBatchYear());
    user.setBatchSemester(registerRequest.getBatchSemester());
    user.setCourseType(registerRequest.getCourseType());

    // Set PRN if present
    if (registerRequest.getPrn() != null) {
      user.setPrn(registerRequest.getPrn());
    }

    // set company if present
    if (registerRequest.getCompany() != null) {
      user.setCompany(registerRequest.getCompany());
    }

    authRepo.save(user);

    //generate jwt token
    String jwtToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getUserType());

    UserResponse response = new UserResponse();
    response.setJwtToken(jwtToken);
    response.setStatus(201);
    return response;

  }

  public UserResponse loginUser(LoginRequest loginRequest) {
    // login logic
    User user = authRepo.findByEmail(loginRequest.getEmail())
      .orElseThrow(() -> new RuntimeException("Invalid credentials"));

    if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new RuntimeException("Invalid credentials");
    }

    // successful login
    String jwtToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getUserType());
    UserResponse response = new UserResponse();
    response.setJwtToken(jwtToken);
    response.setStatus(200);
    return response;
  }
}

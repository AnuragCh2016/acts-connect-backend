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

    public AuthService(final AuthRepo authRepo) {
        this.authRepo = authRepo;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = new JwtUtil();
    }

    public UserResponse registerUser(final RegisterRequest registerRequest) {
        // Check if email already exists
        if (this.authRepo.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create a new user
        final User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(this.bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setUserType(registerRequest.getUserType());
        user.setName(registerRequest.getName());
        user.setBatchYear(registerRequest.getBatchYear());
        user.setBatchSemester(registerRequest.getBatchSemester());
        user.setCourseType(registerRequest.getCourseType());

        // Set PRN if present
        if (null != registerRequest.getPrn()) {
            user.setPrn(registerRequest.getPrn());
        }

        // Set company if present
        if (null != registerRequest.getCompany()) {
            user.setCompany(registerRequest.getCompany());
        }

        this.authRepo.save(user);

        // Generate JWT token
        final String jwtToken = this.jwtUtil.generateToken(user.getId(), user.getEmail(), user.getUserType());

        // Create response
        final UserResponse response = new UserResponse();
        response.setJwtToken(jwtToken);
        response.setStatus(201);
        return response;
    }

    public UserResponse loginUser(final LoginRequest loginRequest) {
        // Fetch user by email
        final User user = this.authRepo.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Validate password
        if (!this.bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Successful login: generate JWT token
        final String jwtToken = this.jwtUtil.generateToken(user.getId(), user.getEmail(), user.getUserType());
        final UserResponse response = new UserResponse();
        response.setJwtToken(jwtToken);
        response.setStatus(200);
        return response;
    }
}

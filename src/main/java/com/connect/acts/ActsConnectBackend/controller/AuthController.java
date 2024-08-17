package com.connect.acts.ActsConnectBackend.controller;

import com.connect.acts.ActsConnectBackend.dto.LoginRequest;
import com.connect.acts.ActsConnectBackend.dto.RegisterRequest;
import com.connect.acts.ActsConnectBackend.dto.UserResponse;
import com.connect.acts.ActsConnectBackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  //public constructor AuthController
  public AuthController(final AuthService authService) {
    this.authService = authService;
  }

  //public method login
  @PostMapping("/login")
  public ResponseEntity<UserResponse> login(@RequestBody @Valid final LoginRequest loginRequest) {
    final UserResponse response = this.authService.loginUser(loginRequest);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  //public method register
  @PostMapping("/register")
  public ResponseEntity<UserResponse> register(@RequestBody @Valid final RegisterRequest registerRequest) {
    final UserResponse response = this.authService.registerUser(registerRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}

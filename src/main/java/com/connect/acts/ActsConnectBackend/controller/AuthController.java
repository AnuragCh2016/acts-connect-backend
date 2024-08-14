package com.connect.acts.ActsConnectBackend.controller;

import com.connect.acts.ActsConnectBackend.dto.LoginRequest;
import com.connect.acts.ActsConnectBackend.dto.RegisterRequest;
import com.connect.acts.ActsConnectBackend.dto.UserResponse;
import com.connect.acts.ActsConnectBackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  //public method login
  @PostMapping("/login")
  public ResponseEntity<UserResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
    UserResponse response = authService.loginUser(loginRequest);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  //public method register
  @PostMapping("/register")
  public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
    UserResponse response = authService.registerUser(registerRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}

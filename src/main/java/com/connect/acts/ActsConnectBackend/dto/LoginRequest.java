package com.connect.acts.ActsConnectBackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequest {
  @NotBlank(message = "Email is required")
  private final String email;

  @NotBlank(message = "Password is required")
  private final String password;

}

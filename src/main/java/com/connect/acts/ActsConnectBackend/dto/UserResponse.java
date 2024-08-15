package com.connect.acts.ActsConnectBackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/*
* User response will contain the jwt token and the status of the request
* */
@Data
@Getter
@Setter
public class UserResponse {
  private int status;
  private String jwtToken;
}

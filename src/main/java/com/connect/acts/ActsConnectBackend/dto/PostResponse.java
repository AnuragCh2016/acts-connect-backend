package com.connect.acts.ActsConnectBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostResponse {
  private int status;
  private List<PostDTO> posts;
}

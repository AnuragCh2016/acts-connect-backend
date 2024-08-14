package com.connect.acts.ActsConnectBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
public class PostDTO {
  private UUID id;
  private String title;
  private String content;
  private boolean isDummy;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UUID userId; // minimal user info
  private String name; // minimal user info
}

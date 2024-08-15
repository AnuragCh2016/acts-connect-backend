package com.connect.acts.ActsConnectBackend.dto;

import com.connect.acts.ActsConnectBackend.model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponseDTO {
  private UUID id;
  private String name;
  private String email;
  private String company;
  private Course courseType;
  private int batchYear;
}

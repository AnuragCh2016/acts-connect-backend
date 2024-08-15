package com.connect.acts.ActsConnectBackend.dto;

import com.connect.acts.ActsConnectBackend.model.Course;
import lombok.Data;

import java.time.Year;

@Data
public class UserSearchRequest {
  private String name;
  private Integer minBatchYear;
  private Integer maxBatchYear = Year.now().getValue();
  private String company;
  private Course courseType;
}
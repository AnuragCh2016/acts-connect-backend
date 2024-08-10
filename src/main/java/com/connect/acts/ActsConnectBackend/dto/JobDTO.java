package com.connect.acts.ActsConnectBackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class JobDTO {
    private UUID id; // Optional, included for update
    private String companyName;
    private String jobTitle;
    private String jobDescription; // Ideally in markdown
    private String jobLocation;
    private String jobType; // Enum name as String
    private int minExperience;
    private double salary;
    private int numOpenPositions;
    private String applicationLink;
    private String jobStatus; // Enum name as String
    private LocalDateTime createdAt; // Optional, auto-populated
    private LocalDateTime updatedAt; // Optional, auto-populated
}

package com.connect.acts.ActsConnectBackend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class JobApplicationDTO {
    private UUID jobId;
    private String coverLetter;
    private String resumeLink;  // Link to the applicant's resume
}

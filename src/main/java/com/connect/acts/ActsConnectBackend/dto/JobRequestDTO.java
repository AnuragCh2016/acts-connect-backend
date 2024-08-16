package com.connect.acts.ActsConnectBackend.dto;

import com.connect.acts.ActsConnectBackend.model.JobStatus;
import com.connect.acts.ActsConnectBackend.model.JobType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobRequestDTO {

    private String companyName;

    private String jobTitle;

    private String jobDescription;  // ideally in markdown

    private String jobLocation;

    private JobType jobType = JobType.FULL_TIME; // Default to FULL_TIME

    private int minExperience;

    private double salary;

    private int numOpenPositions;

    private String applicationLink;

    private JobStatus jobStatus = JobStatus.OPEN; // Default to OPEN
}

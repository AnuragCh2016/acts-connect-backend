package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.repo.JobRepo;
import org.springframework.stereotype.Service;

@Service
public class JobService {
  private final JobRepo jobRepo;

  public JobService(JobRepo jobRepo) {
    this.jobRepo = jobRepo;
  }
}

package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.JobApplicationDTO;
import com.connect.acts.ActsConnectBackend.dto.JobDTO;
import com.connect.acts.ActsConnectBackend.dto.JobRequestDTO;
import com.connect.acts.ActsConnectBackend.model.Job;
import com.connect.acts.ActsConnectBackend.model.User;
import com.connect.acts.ActsConnectBackend.repo.JobRepo;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepo jobRepo;

    public JobService(JobRepo jobRepo) {
        this.jobRepo = jobRepo;
    }

    public JobDTO createJob(User user, JobRequestDTO jobRequestDTO) {
        Job job = new Job();
        job.setCompanyName(jobRequestDTO.getCompanyName());
        job.setJobTitle(jobRequestDTO.getJobTitle());
        job.setJobDescription(jobRequestDTO.getJobDescription());
        job.setJobLocation(jobRequestDTO.getJobLocation());
        job.setJobType(jobRequestDTO.getJobType());
        job.setMinExperience(jobRequestDTO.getMinExperience());
        job.setSalary(jobRequestDTO.getSalary());
        job.setNumOpenPositions(jobRequestDTO.getNumOpenPositions());
        job.setApplicationLink(job.getApplicationLink());
        job.setJobStatus(jobRequestDTO.getJobStatus());
        Job savedJob = jobRepo.save(job);
        return new JobDTO(savedJob);
    }

    public boolean deleteJob(User user, UUID jobId) {
        if (jobRepo.existsById(jobId)) {
            jobRepo.deleteById(jobId);
            return true;
        }
        return false;
    }

    public JobDTO updateJob(User user, UUID jobId, @Valid JobRequestDTO jobRequestDTO) {
        if (jobRepo.existsById(jobId)) {
            Job job = jobRepo.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
            job.setCompanyName(jobRequestDTO.getCompanyName());
            job.setJobTitle(jobRequestDTO.getJobTitle());
            job.setJobDescription(jobRequestDTO.getJobDescription());
            job.setJobLocation(jobRequestDTO.getJobLocation());
            job.setJobType(jobRequestDTO.getJobType());
            job.setMinExperience(jobRequestDTO.getMinExperience());
            job.setSalary(jobRequestDTO.getSalary());
            job.setNumOpenPositions(jobRequestDTO.getNumOpenPositions());
            job.setApplicationLink(jobRequestDTO.getApplicationLink());
            job.setJobStatus(jobRequestDTO.getJobStatus());
            Job updatedJob = jobRepo.save(job);
            return new JobDTO(updatedJob);
        } else {
            throw new RuntimeException("Job not found");
        }
    }

    public List<JobDTO> getJobs() {
        return jobRepo.findAll().stream().map(JobDTO::new).collect(Collectors.toList());
    }

    public boolean applyForJob(User user, JobApplicationDTO jobApplicationDTO) {
        Job job = jobRepo.findById(jobApplicationDTO.getJobId()).orElse(null);
        if (job == null) {
            return false;
        }

        //TODO - Complete the functionality for the method

        return true;  // Assuming the application was successfully processed
    }

}

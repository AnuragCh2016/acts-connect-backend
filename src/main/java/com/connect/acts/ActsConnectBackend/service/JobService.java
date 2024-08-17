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

    public JobService(final JobRepo jobRepo) {
        this.jobRepo = jobRepo;
    }

    public JobDTO createJob(final User user, final JobRequestDTO jobRequestDTO) {
        final Job job = new Job();
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
        final Job savedJob = this.jobRepo.save(job);
        return new JobDTO(savedJob);
    }

    public boolean deleteJob(final User user, final UUID jobId) {
        if (this.jobRepo.existsById(jobId)) {
            this.jobRepo.deleteById(jobId);
            return true;
        }
        return false;
    }

    public JobDTO updateJob(final User user, final UUID jobId, @Valid final JobRequestDTO jobRequestDTO) {
        if (this.jobRepo.existsById(jobId)) {
            final Job job = this.jobRepo.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
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
            final Job updatedJob = this.jobRepo.save(job);
            return new JobDTO(updatedJob);
        } else {
            throw new RuntimeException("Job not found");
        }
    }

    public List<JobDTO> getJobs() {
        return this.jobRepo.findAll().stream().map(JobDTO::new).collect(Collectors.toList());
    }

    public boolean applyForJob(final User user, final JobApplicationDTO jobApplicationDTO) {
        final Job job = this.jobRepo.findById(jobApplicationDTO.getJobId()).orElse(null);
        return null != job;

        //TODO - Complete the functionality for the method
// Assuming the application was successfully processed
    }

}

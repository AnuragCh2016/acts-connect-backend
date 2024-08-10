package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.JobDTO;
import com.connect.acts.ActsConnectBackend.exception.ResourceNotFoundException;
import com.connect.acts.ActsConnectBackend.model.Job;
import com.connect.acts.ActsConnectBackend.model.JobStatus;
import com.connect.acts.ActsConnectBackend.model.JobType;
import com.connect.acts.ActsConnectBackend.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobDTO createJob(JobDTO jobDto) {
        Job job = new Job();
        job.setCompanyName(jobDto.getCompanyName());
        job.setJobTitle(jobDto.getJobTitle());
        job.setJobDescription(jobDto.getJobDescription());
        job.setJobLocation(jobDto.getJobLocation());
        job.setJobType(JobType.valueOf(jobDto.getJobType()));
        job.setMinExperience(jobDto.getMinExperience());
        job.setSalary(jobDto.getSalary());
        job.setNumOpenPositions(jobDto.getNumOpenPositions());
        job.setApplicationLink(jobDto.getApplicationLink());
        job.setJobStatus(JobStatus.valueOf(jobDto.getJobStatus()));

        Job savedJob = jobRepository.save(job);

        // Map saved Job to JobDTO
        jobDto.setId(savedJob.getId());
        return jobDto;
    }

    public JobDTO updateJob(UUID jobId, JobDTO jobDto) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isEmpty()) {
            throw new ResourceNotFoundException("Job not found with ID: " + jobId);
        }

        Job job = jobOptional.get();
        job.setCompanyName(jobDto.getCompanyName());
        job.setJobTitle(jobDto.getJobTitle());
        job.setJobDescription(jobDto.getJobDescription());
        job.setJobLocation(jobDto.getJobLocation());
        job.setJobType(JobType.valueOf(jobDto.getJobType()));
        job.setMinExperience(jobDto.getMinExperience());
        job.setSalary(jobDto.getSalary());
        job.setNumOpenPositions(jobDto.getNumOpenPositions());
        job.setApplicationLink(jobDto.getApplicationLink());
        job.setJobStatus(JobStatus.valueOf(jobDto.getJobStatus()));

        Job updatedJob = jobRepository.save(job);

        // Map updated Job to JobDTO
        jobDto.setId(updatedJob.getId());
        return jobDto;
    }

    public void deleteJob(UUID jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isEmpty()) {
            throw new ResourceNotFoundException("Job not found with ID: " + jobId);
        }
        jobRepository.deleteById(jobId);
    }

    public JobDTO getJobById(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + jobId));
        return mapToDTO(job);
    }

    public List<JobDTO> getAllJobs() {
        return jobRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private JobDTO mapToDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setCompanyName(job.getCompanyName());
        dto.setJobTitle(job.getJobTitle());
        dto.setJobDescription(job.getJobDescription());
        dto.setJobLocation(job.getJobLocation());
        dto.setJobType(job.getJobType().name());
        dto.setMinExperience(job.getMinExperience());
        dto.setSalary(job.getSalary());
        dto.setNumOpenPositions(job.getNumOpenPositions());
        dto.setApplicationLink(job.getApplicationLink());
        dto.setJobStatus(job.getJobStatus().name());
        return dto;
    }
}

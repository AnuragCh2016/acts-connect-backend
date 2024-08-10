package com.connect.acts.ActsConnectBackend.repository;

import com.connect.acts.ActsConnectBackend.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
}

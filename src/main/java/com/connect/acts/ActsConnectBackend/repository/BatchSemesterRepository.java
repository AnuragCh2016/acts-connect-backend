package com.connect.acts.ActsConnectBackend.repository;

import com.connect.acts.ActsConnectBackend.model.BatchSemester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatchSemesterRepository extends JpaRepository<BatchSemester, Long> {
    Optional<BatchSemester> findByName(String name);
}

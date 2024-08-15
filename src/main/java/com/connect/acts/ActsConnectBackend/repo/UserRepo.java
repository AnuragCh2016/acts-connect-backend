package com.connect.acts.ActsConnectBackend.repo;

import com.connect.acts.ActsConnectBackend.model.Course;
import com.connect.acts.ActsConnectBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
  User findByEmail(String email);

  @Query("SELECT u FROM User u WHERE " +
    "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
    "(:minBatchYear IS NULL OR u.batchYear >= :minBatchYear) AND " +
    "(:maxBatchYear IS NULL OR u.batchYear <= :maxBatchYear) AND " +
    "(:company IS NULL OR LOWER(u.company) LIKE LOWER(CONCAT('%', :company, '%'))) AND " +
    "(:courseType IS NULL OR u.courseType = :courseType)")
  List<User> searchUsers(
    @Param("name") String name,
    @Param("minBatchYear") Integer minBatchYear,
    @Param("maxBatchYear") Integer maxBatchYear,
    @Param("company") String company,
    @Param("courseType") Course courseType);

}

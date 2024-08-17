package com.connect.acts.ActsConnectBackend.dto;

import com.connect.acts.ActsConnectBackend.model.BatchSemester;
import com.connect.acts.ActsConnectBackend.model.Course;
import com.connect.acts.ActsConnectBackend.model.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Month;


@Data
@Getter
@Setter
public class RegisterRequest {
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  private String password;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Batch year is required")
  private int batchYear;

  private UserType userType; // Should match UserType enum values

  @NotBlank(message = "Course type is required")
  private Course courseType; // Should match Course enum values

  @NotBlank(message = "Batch semester is required")
  private BatchSemester batchSemester; // Should match BatchSemester enum values

  @Pattern(regexp = "\\d{12}", message = "PRN must be exactly 12 digits")
  private String prn; // PRN is optional

  private String company;

  // Getters and Setters
  public UserType getUserType() {
    if (null == this.userType) {
      this.userType = this.calculateUserType();
    }
    return this.userType;
  }

  public UserType calculateUserType() {
    final LocalDate currentDate = LocalDate.now();
    final LocalDate programEndDate = LocalDate.of(this.batchYear, this.getSemesterMonth(this.batchSemester), 1); // Assuming program starts on 1st

    return programEndDate.plusMonths(6).isBefore(currentDate) ? UserType.ALUMNI : UserType.STUDENT;
  }

  private Month getSemesterMonth(final BatchSemester semester) {
    return BatchSemester.MARCH == semester ? Month.MARCH : Month.SEPTEMBER;
  }

}

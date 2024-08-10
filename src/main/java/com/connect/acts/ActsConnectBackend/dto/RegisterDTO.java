package com.connect.acts.ActsConnectBackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private int batchYear;

    private String userType; // Should be converted to UserType enum during processing

    private String courseType; // Should be converted to Course enum during processing

    private String batchSemester; // Should be converted to BatchSemester enum during processing

    // Optional fields
    private String company;
    private String profilePictureUrl;
    private String bio;
    private String location;
}

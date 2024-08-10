package com.connect.acts.ActsConnectBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    // Common fields for registration and update
    private String email;
    private String password; // For registration only
    private String name;
    private int batchYear;
    private String userType; // For example, "STUDENT", "ALUMNI"
    private String courseType; // For example, "DAC", "DBDA"
    private String batchSemester; // For example, "MARCH", "SEPTEMBER"
    private String bio;
    private String location;
    private String company;

    // Optionally include an ID for update operations
    private String id; // Use UUID as a string for easier handling

    // Add a field for user status if needed
    @Getter
    private String userStatus; // For example, "ACTIVE", "INACTIVE"

}

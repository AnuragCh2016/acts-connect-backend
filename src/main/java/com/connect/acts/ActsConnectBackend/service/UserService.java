package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.UserDTO;
import com.connect.acts.ActsConnectBackend.exception.ResourceNotFoundException;
import com.connect.acts.ActsConnectBackend.model.*;
import com.connect.acts.ActsConnectBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserDTO userDto) {
        // Create a new User entity from the DTO
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // Consider hashing the password before saving
        user.setUserType(UserType.valueOf(userDto.getUserType().toUpperCase()));

        user.setUserStatus(UserStatus.ACTIVE); // Default status
        user.setBatchSemester(BatchSemester.valueOf(userDto.getBatchSemester().toUpperCase())); // Ensure proper
        // conversion
        user.setCourseType(Course.valueOf(userDto.getCourseType().toUpperCase())); // Ensure proper conversion
        user.setBatchYear(userDto.getBatchYear());
        user.setBio(userDto.getBio());
        user.setLocation(userDto.getLocation());
        user.setCompany(userDto.getCompany());

        // Save the user
        userRepository.save(user);
    }

    public void updateUser(UUID userId, UserDTO userDto) {
        // Find the existing user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Update the user entity with new details from DTO
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // Consider hashing the password before saving
        user.setUserType(UserType.valueOf(userDto.getUserType().toUpperCase()));
        user.setUserStatus(UserStatus.valueOf(userDto.getUserStatus().toUpperCase())); // Ensure proper conversion
        user.setBatchSemester(BatchSemester.valueOf(userDto.getBatchSemester().toUpperCase())); // Ensure proper
        // conversion
        user.setCourseType(Course.valueOf(userDto.getCourseType().toUpperCase())); // Ensure proper conversion
        user.setBatchYear(userDto.getBatchYear());
        user.setBio(userDto.getBio());
        user.setLocation(userDto.getLocation());
        user.setCompany(userDto.getCompany());

        // Save and return the updated user
        userRepository.save(user);
    }

    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId().toString());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setBatchYear(user.getBatchYear());
        userDto.setUserType(user.getUserType().name());
        userDto.setCourseType(user.getCourseType().name());
        userDto.setBatchSemester(user.getBatchSemester().name());
        userDto.setBio(user.getBio());
        userDto.setLocation(user.getLocation());
        userDto.setCompany(user.getCompany());
        userDto.setUserStatus(user.getStatus().name());

        return userDto;
    }
}

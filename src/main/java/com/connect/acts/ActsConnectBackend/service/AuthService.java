package com.connect.acts.ActsConnectBackend.service;

import com.connect.acts.ActsConnectBackend.dto.LoginDTO;
import com.connect.acts.ActsConnectBackend.dto.RegisterDTO;
import com.connect.acts.ActsConnectBackend.dto.TokenDTO;
import com.connect.acts.ActsConnectBackend.model.*;
import com.connect.acts.ActsConnectBackend.repository.UserRepository;
import com.connect.acts.ActsConnectBackend.util.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public void register(RegisterDTO registerDto) {

        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setBatchYear(registerDto.getBatchYear());
        user.setCourseType(Course.valueOf(registerDto.getCourseType()));
        user.setUserType(UserType.valueOf(registerDto.getUserType().toUpperCase())); // Assuming
        // registerDto.getUserType()
        // returns a string

        // Convert string to BatchSemester enum
        BatchSemester batchSemester = BatchSemester.valueOf(registerDto.getBatchSemester().toUpperCase());
        user.setBatchSemester(batchSemester);

        user.setApproved(false); // Default to not approved
        user.setStatus(UserStatus.ACTIVE); // Default to active

        // Save the new user
        userRepository.save(user);
    }

    public TokenDTO login(LoginDTO LoginDTO) {
        // Authenticate user
        Optional<User> userOptional = userRepository.findByEmail(LoginDTO.getEmail());
        if (userOptional.isEmpty()
                || !passwordEncoder.matches(LoginDTO.getPassword(), userOptional.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOptional.get();

        // Generate tokens
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        return new TokenDTO(accessToken, refreshToken);
    }
}

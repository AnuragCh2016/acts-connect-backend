package com.connect.acts.ActsConnectBackend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class PostDTO {

    // Unique identifier for the post, can be null for creation
    private UUID id;

    // Title of the post, should not be empty
    @NotEmpty(message = "Title cannot be empty")
    @Size(max = 100, message = "Title should not exceed 100 characters")
    private String title;

    // Content of the post, should not be empty
    @NotEmpty(message = "Content cannot be empty")
    private String content;

    // ID of the user creating or updating the post
    @NotNull(message = "User ID cannot be null")
    private UUID userId;

    // IDs of users who liked the post
    private Set<UUID> likedByUsers;

    // Automatically set during creation
    private LocalDateTime createdAt;

    // Automatically set during updates
    private LocalDateTime updatedAt;
}
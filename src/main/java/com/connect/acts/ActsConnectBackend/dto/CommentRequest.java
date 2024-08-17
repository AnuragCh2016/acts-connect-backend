package com.connect.acts.ActsConnectBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Data
public class CommentRequest {

    @NotBlank(message = "Text is required")
    private String text;

    @NotNull(message = "Post ID is required")
    private UUID postId;

    // Getter and Setter methods (if not using Lombok)
    @NotNull(message = "Comment ID is required")
    private UUID commentId;

}

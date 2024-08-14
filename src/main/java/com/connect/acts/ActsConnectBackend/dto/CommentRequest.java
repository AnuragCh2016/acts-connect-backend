package com.connect.acts.ActsConnectBackend.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CommentRequest {
    @NotBlank(message = "Text is required")
    private String text;
    private UUID postId;
}

package com.connect.acts.ActsConnectBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CommentResponse {
    private UUID id;
    private String text;
    private LocalDateTime createdAt;
}

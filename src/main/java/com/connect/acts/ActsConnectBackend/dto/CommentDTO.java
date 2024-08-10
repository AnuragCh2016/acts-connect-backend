package com.connect.acts.ActsConnectBackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
public class CommentDTO {

    // Getters and Setters
    private UUID id;
    private String text;
    private UUID postId;
    private UUID userId;
    private UUID parentCommentId;
    private Date createdDate;
    private Date modifiedDate;

}

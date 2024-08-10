package com.connect.acts.ActsConnectBackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FollowRequestDTO {
    private UUID followerId;
}

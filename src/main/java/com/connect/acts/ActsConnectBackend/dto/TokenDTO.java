package com.connect.acts.ActsConnectBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDTO {

    // Getters and Setters
    private String accessToken;
    private String refreshToken;

    // Constructors
    public TokenDTO() {
    }

    public TokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}

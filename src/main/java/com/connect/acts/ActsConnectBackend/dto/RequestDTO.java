package com.connect.acts.ActsConnectBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestDTO {

    // Getters and Setters
    private String id;
    private String name;
    private String description;

    // Constructors
    public RequestDTO() {
    }

    public RequestDTO(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

}

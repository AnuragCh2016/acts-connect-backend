package com.connect.acts.ActsConnectBackend.model;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return description;
    }
}

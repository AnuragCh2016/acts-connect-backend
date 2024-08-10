package com.connect.acts.ActsConnectBackend.model;

public enum JobStatus {
    OPEN("Job is open and accepting applications"),
    CLOSED("Job is closed and no longer accepting applications"),
    FILLED("Job has been filled and the position is no longer available"),
    EXPIRED("Job posting has expired and is no longer active");

    private final String description;

    JobStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // Optional: A method to get all enum values
    public static JobStatus[] getAllStatuses() {
        return values();
    }
}

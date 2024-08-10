package com.connect.acts.ActsConnectBackend.model;

public enum JobType {
    FULL_TIME("Full-time job"),
    PART_TIME("Part-time job"),
    INTERNSHIP("Internship"),
    CONTRACT("Contract job"),
    FREELANCE("Freelance job"),
    REMOTE("Remote job"),
    VOLUNTEER("Volunteer job");

    private final String description;

    JobType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}

package com.connect.acts.ActsConnectBackend.model;

public enum UserType {
    STUDENT("Student"),
    ALUMNI("Alumni"),
    TEACHER("Teacher"),
    ADMIN("Admin"),
    FOUNDER("Founder");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    // Optional: Method to get UserType by description
    public static UserType fromDescription(String description) {
        for (UserType type : UserType.values()) {
            if (type.getDescription().equalsIgnoreCase(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with description " + description);
    }
}

package com.connect.acts.ActsConnectBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(length = 12)
    private String prn;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int batchYear; // Assume this represents the year of the batch the user belongs to

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Course courseType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchSemester batchSemester;

    @ManyToMany(mappedBy = "likedByUsers")
    private Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>();

    @Column
    private String company; // Optional, company where the user is currently working

    @Column
    private String profilePictureUrl; // URL to the user's profile picture

    @Column(nullable = false)
    private boolean approved = false; // Approval status of the user

    @Column(nullable = false)
    private LocalDateTime createdAt; // Timestamp when the user was created

    @Column(nullable = false)
    private LocalDateTime updatedAt; // Timestamp when the user was last updated

    @Enumerated(EnumType.STRING)
    private UserStatus status; // Status of the user (e.g., active, inactive)

    @Column(length = 500)
    private String bio; // Short bio or description of the user

    @Column(length = 100)
    private String location; // User's location or city

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setUserStatus(UserStatus status) {
        this.status = status;
    }
}

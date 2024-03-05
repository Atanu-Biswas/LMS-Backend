package com.project.library.management.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false)
    private String name;
    private String address;
    @Column(nullable = false)
    private String mobileNo;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean isActive;
    @OneToMany(mappedBy = "user")
    private List<BookIssue> bookIssues;
    @OneToMany(mappedBy = "user")
    private List<BookRequest> bookRequests;
    // Getters and setters omitted for brevity
}


package com.project.library.management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

import org.springframework.core.annotation.Order;

@Data
@Entity
@Table(name = "book_request")
@Order(3)
public class BookRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Date requestDate;

    @Column(nullable = false)
    private int weeksRequested;

    @Column(nullable = false)
    private String status = "PENDING";
    // Getters and setters omitted for brevity
}


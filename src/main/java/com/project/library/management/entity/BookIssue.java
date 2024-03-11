package com.project.library.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.Date;

@Data
@Entity
@Table(name = "book_issue")
@Order(3)
public class BookIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Date issueDate;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private Date returnDate;



   // Getters and setters omitted for brevity
}

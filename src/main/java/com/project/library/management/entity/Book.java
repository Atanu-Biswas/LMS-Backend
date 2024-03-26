package com.project.library.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN; // Import for ISBN validation
import org.springframework.core.annotation.Order;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "book")
@Order(2)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false, unique = true) // Enforce ISBN uniqueness
    @ISBN  // Add ISBN validation annotation
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    @Min(value = 1501, message = "Print year must be greater than 1500")  // Validation message
    private int printYear;  // Changed to int for year

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    @Min(1)
    private int quantityAvailable;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private Date dateAdded;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)

    private Publisher publisher;

    @OneToMany(mappedBy = "book")
    private List<BookIssue> bookIssues;

    @OneToMany(mappedBy = "book")
    private List<BookRequest> bookRequests;

    // Getters and setters omitted for brevity
}

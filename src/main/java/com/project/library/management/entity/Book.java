package com.project.library.management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private int printYear;

    private String imageUrl;

    @Column(nullable = false)
    private int quantityAvailable;


    private boolean isActive;



    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)

    private Publisher publisher;

    @OneToMany(mappedBy = "book")
    private List<BookIssue> bookIssues;

    @OneToMany(mappedBy = "book")
    private List<BookRequest> bookRequests;

    // Getters and setters omitted for brevity
}

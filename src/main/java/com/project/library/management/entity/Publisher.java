package com.project.library.management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import org.springframework.core.annotation.Order;

@Data
@Entity
@Table(name = "publisher")
@Order(1)
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherId;
    @Column(nullable = false)
    private String name;
    private String email;
    private String phoneNo;
    @OneToMany(mappedBy = "publisher")
    private List<Book> books;
    // Getters and setters omitted for brevity
}


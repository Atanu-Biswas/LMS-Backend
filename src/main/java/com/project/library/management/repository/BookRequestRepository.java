package com.project.library.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.library.management.entity.BookRequest;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
    
}

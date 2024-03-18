package com.project.library.management.repository;

import com.project.library.management.entity.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
    //filter it by sending only the books that have status PENDING
    List<BookRequest> findTop10ByOrderByRequestDateDesc();

    List<BookRequest> findByUserUsername(String username);
}

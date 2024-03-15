package com.project.library.management.repository;

import com.project.library.management.entity.Book;
import com.project.library.management.entity.BookRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
  
    @Query("SELECT bookRequest FROM BookRequest bookRequest WHERE bookRequest.user.username = :userName")
    List<BookRequest> findAllRequestByUserId(String userName);
}

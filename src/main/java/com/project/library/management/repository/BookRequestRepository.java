package com.project.library.management.repository;

import com.project.library.management.entity.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.library.management.entity.User;
import com.project.library.management.entity.Book;


import java.util.List;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

    BookRequest findByUserAndBook(User user, Book book);
    List<BookRequest> findTop10ByOrderByRequestDateDesc();

    List<BookRequest> findByUserUsername(String username);
}

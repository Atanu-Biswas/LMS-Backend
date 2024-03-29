package com.project.library.management.repository;

import com.project.library.management.entity.Book;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book,Long>{




    Page<Book> findByTitleStartingWithIgnoreCase(String title, Pageable p);

    @Query("select book from Book book where lower(book.title) like lower(concat('%', :title, '%'))")
    Book  findBookByTitle(String title);

    Page<Book> findByAuthorStartingWithIgnoreCase(String authorName, Pageable p);

    List<Book> findByDateAddedBetween(Date startDate, Date endDate);


    Book findByBookId(Long id);

    void deleteByBookId(Long bookId);
}

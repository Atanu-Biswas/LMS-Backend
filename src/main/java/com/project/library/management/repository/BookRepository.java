package com.project.library.management.repository;

import com.project.library.management.entity.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long>{




    Page<Book> findByTitleStartingWithIgnoreCase(String title, Pageable p);

    @Query("select book from Book book where lower(book.title) like lower(concat('%', :title, '%'))")
    Book  findBookByTitle(String title);

    Page<Book> findByAuthorStartingWithIgnoreCase(String authorName, Pageable p);


    @Query("SELECT b FROM Book b WHERE LOWER(b.publisher.name) = LOWER(:publisherName)")
    List<Book> findByPublisher(String publisherName);


    Book findByBookId(Long id);

    void deleteByBookId(Long bookId);

    @Query(value = "SELECT genre, COUNT(*) FROM TABLE ( Book ) GROUP BY genre", nativeQuery = true)
    List<Object[]> getGenreWiseSplit();
}

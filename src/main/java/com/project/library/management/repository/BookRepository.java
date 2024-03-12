package com.project.library.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.library.management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long>{
    Book  findBookByTitle(String name);

    @Query("select book from Book book where lower(book.author) like lower(concat('%', :authorname, '%'))")
    List<Book> findByAuthorIgnoreCase(String authorname);
 
     @Query("select book from Book book where book.bookId= :id")  
     Book findByBookId(Long id);
}

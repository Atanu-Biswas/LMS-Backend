package com.project.library.management.repository;

import com.project.library.management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long>{
    


    @Query("select book from Book book where lower(book.title) like lower(concat('%', :title, '%'))")
    List<Book>  findBookListByTitle(String title);

    @Query("select book from Book book where lower(book.title) like lower(concat('%', :title, '%'))")
    Book  findBookByTitle(String title);

    @Query("select book from Book book where lower(book.author) like lower(concat('%', :authorName, '%'))")
    List<Book> findByAuthorIgnoreCase(String authorName);


    @Query("SELECT b FROM Book b WHERE LOWER(b.publisher.name) = LOWER(:publisherName)")
    List<Book> findByPublisher(String publisherName);
 
     @Query("select book from Book book where book.bookId= :id")  
     Book findByBookId(Long id);
}

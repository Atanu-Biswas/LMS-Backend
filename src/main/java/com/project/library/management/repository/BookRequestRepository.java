package com.project.library.management.repository;

import com.project.library.management.entity.Book;
import com.project.library.management.entity.BookRequest;
import com.project.library.management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {

    @Query("SELECT bookRequest FROM BookRequest bookRequest WHERE bookRequest.user.username = :userName")
    List<BookRequest> findAllRequestByUserId(String userName);

    List<BookRequest> findListByUserAndBook(User user, Book book);
    BookRequest findByUserAndBook(User user, Book book);

    //filter it by sending only the books that have status PENDING
    List<BookRequest> findTop10ByOrderByRequestDateDesc();

    List<BookRequest> findByUserUsername(String username);

    @Query("SELECT br FROM BookRequest br WHERE br.status = 'PENDING'")
    Page<BookRequest> findAllPendingRequests(Pageable p);
    

    //keep this 
    @Query("SELECT br FROM BookRequest br WHERE br.user.username= :username AND br.status = 'PENDING'")
    Page<BookRequest> findAllPendingRequestsByUserName(String username,Pageable p);





}

package com.project.library.management.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.project.library.management.entity.Book;
import com.project.library.management.entity.BookIssue;
import com.project.library.management.entity.BookRequest;
import com.project.library.management.entity.User;
import com.project.library.management.repository.BookIssueRepository;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.BookRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookIssueService {

    private final BookIssueRepository bookIssueRepository;
    private final BookRequestRepository bookRequestRepository;
    private final BookRepository bookRepository;

    public boolean acceptReturn(Long issueId){

        boolean isReturnAccepted=false;

        BookIssue bookIssue = bookIssueRepository.findById(issueId).orElse(null);
        
        //setting return date as current system date
        bookIssue.setReturnDate(new Date());  

        //set status of that book as returned in book request table
        User user= bookIssue.getUser();
        Book book= bookIssue.getBook();
        BookRequest bookRequest= bookRequestRepository.findByUserAndBook(user,book);
        bookRequest.setStatus("RETURNED");

        //increase the quantity available of that book by one
        book.setQuantityAvailable(book.getQuantityAvailable()+1);

        bookIssueRepository.save(bookIssue);
        bookRequestRepository.save(bookRequest);
        bookRepository.save(book);
        
        isReturnAccepted=true;

        return isReturnAccepted;
    }

    
}

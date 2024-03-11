package com.project.library.management.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.entity.Book;
import com.project.library.management.entity.BookRequest;
import com.project.library.management.entity.User;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BookRequestService {
    private final BookRequestRepository bookRequestRepository;
    private final BookRepository bookRepository;

    private final UserRepository userRepository;
    public boolean issueRequest(String username, BookRequestDTO bookRequestDTO) {
        User user = userRepository.findByUsername(username).orElse(null);
        System.out.println("user: "+user);
        Book book = bookRepository.findById(bookRequestDTO.getBookId()).orElse(null);
        if (user==null || book==null) {
            return false;
        }
        BookRequest request = new BookRequest();
        request.setUser(user);
        request.setBook(book);
        request.setRequestDate(new Date());
        request.setWeeksRequested(bookRequestDTO.getWeeksRequested());
        System.out.println("request: "+request);
       // bookRequestRepository.save(request);
        bookRequestRepository.save(request);
        System.out.println("request: "+request);
        return true;
    }

    public boolean cancelRequest(Long requestId) {
        BookRequest request = bookRequestRepository.findById(requestId).orElse(null);
        if (request!=null && request.getStatus().equals("PENDING")) {
            request.setStatus("CANCELLED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }

    public boolean acceptRequest(Long requestId) {
        BookRequest request = bookRequestRepository.findById(requestId).orElse(null);
        if (request!=null && request.getStatus().equals("PENDING")) {
            request.setStatus("ACCEPTED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }

    public boolean declineRequest(Long requestId) {
        BookRequest request = bookRequestRepository.findById(requestId).orElse(null);
        if (request!=null && request.getStatus().equals("PENDING")) {
            request.setStatus("REJECTED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }
}

package com.project.library.management.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.entity.Book;
import com.project.library.management.entity.BookRequest;
import com.project.library.management.entity.User;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookRequestService {
    private final BookRequestRepository bookRequestRepository;
    private final BookRepository bookRespository;
    private final UserRepository userRepository;

    public boolean issueRequest(String username, BookRequestDTO bookRequestDTO) {
        User user = userRepository.findByUsername(username).orElse(null);
        Book book = bookRespository.findById(bookRequestDTO.getBookId()).orElse(null);
        if (book==null) {
            return false;
        }
        BookRequest request = new BookRequest();
        request.setUser(user);
        request.setBook(book);
        request.setRequestDate(new Date());
        request.setWeeksRequested(bookRequestDTO.getWeeksRequested());
        bookRequestRepository.save(request);
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

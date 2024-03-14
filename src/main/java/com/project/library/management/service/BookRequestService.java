package com.project.library.management.service;

import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.dto.UserDTO;
import com.project.library.management.entity.Book;
import com.project.library.management.entity.BookRequest;
import com.project.library.management.entity.User;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookRequestService {
    private final BookRequestRepository bookRequestRepository;
    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    public boolean issueRequest(String username, BookRequestDTO bookRequestDTO) {
        User user = userRepository.findByUsername(username).orElse(null);
        Book book = bookRepository.findById(bookRequestDTO.getBookId()).orElse(null);
        if (book == null) {
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
        if (request != null && request.getStatus().equals("PENDING")) {
            request.setStatus("CANCELLED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }

    public boolean acceptRequest(Long requestId) {
        BookRequest request = bookRequestRepository.findById(requestId).orElse(null);
        if (request != null && request.getStatus().equals("PENDING")) {
            request.setStatus("ACCEPTED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }

    public boolean declineRequest(Long requestId) {
        BookRequest request = bookRequestRepository.findById(requestId).orElse(null);
        if (request != null && request.getStatus().equals("PENDING")) {
            request.setStatus("REJECTED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<BookRequestDTO> viewPendingRequests() {
        String jpql = "SELECT br FROM BookRequest br WHERE br.status = 'PENDING'";
        Query query = entityManager.createQuery(jpql, BookRequest.class);
        List<BookRequest> book_req = query.getResultList();
        List<BookRequestDTO> book_req_dtos = book_req
                .stream()
                .map((req) -> BookRequestDTO
                        .builder()
                        .requestId(req.getRequestId())
                        .bookId(req.getBook().getBookId())
                        .weeksRequested(req.getWeeksRequested())
                        .status(req.getStatus())
                        .username(req.getUser().getUsername())
                        .build())
                .toList();
        return book_req_dtos;
    }
}

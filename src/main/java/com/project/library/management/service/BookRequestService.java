package com.project.library.management.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.entity.Book;
import com.project.library.management.entity.BookRequest;
import com.project.library.management.entity.User;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.repository.BookRespository;
import com.project.library.management.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookRequestService {
    private final BookRequestRepository bookRequestRepository;
    private final BookRespository bookRespository;
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
}

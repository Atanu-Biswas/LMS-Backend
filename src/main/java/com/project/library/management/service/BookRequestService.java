package com.project.library.management.service;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.dto.BookIssueDTO;
import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.dto.ManageBookRequestDTO;
import com.project.library.management.entity.Book;
import com.project.library.management.entity.BookIssue;
import com.project.library.management.entity.BookRequest;
import com.project.library.management.entity.User;
import com.project.library.management.repository.BookIssueRepository;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookRequestService {
    private final BookRequestRepository bookRequestRepository;
    private final BookRepository bookRepository;
    private final BookIssueRepository bookIssueRepository;
    private final UserRepository userRepository;

    public boolean issueRequest(String username, BookRequestDTO bookRequestDTO) {
        User user = userRepository.findByUsername(username).orElse(null);
        Book book = bookRepository.findById(bookRequestDTO.getBookId()).orElse(null);
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

            BookIssue issue = new BookIssue();

            issue.setBook(request.getBook());
            issue.setIssueDate(new Date());

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int daysToAdd = request.getWeeksRequested() * 7;
            calendar.add(Calendar.DATE, daysToAdd);
            Date dueDate = calendar.getTime();
            issue.setDueDate(dueDate);

            issue.setUser(request.getUser());

            bookIssueRepository.save(issue);
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

    public List<ManageBookRequestDTO> getLast10BookRequests() {
        List<BookRequest> last10Requests = bookRequestRepository.findTop10ByOrderByRequestDateDesc();
        return convertToDTOs(last10Requests);
    }

    private List<ManageBookRequestDTO> convertToDTOs(List<BookRequest> bookRequests) {
        List<ManageBookRequestDTO> dtos = new ArrayList<>();
        for (BookRequest request : bookRequests) {
            ManageBookRequestDTO dto = new ManageBookRequestDTO();
            dto.setUsername(request.getUser().getUsername());
            dto.setBookTitle(request.getBook().getTitle());
            dto.setRequestDate(request.getRequestDate());
            dtos.add(dto);
        }
        return dtos;
    }
}

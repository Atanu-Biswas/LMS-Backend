package com.project.library.management.service;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.dto.BookIssueDTO;
import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.dto.ManageBookRequestDTO;
import com.project.library.management.dto.ViewRequestsDTO;
import com.project.library.management.entity.*;
import com.project.library.management.exception.RequestNotFoundException;
import com.project.library.management.repository.BookIssueRepository;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.project.library.management.dto.ManageBookRequestDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookRequestService {
    private static final Logger logger = Logger.getLogger(BookRequestService.class);
    private final BookRequestRepository bookRequestRepository;
    private final BookRepository bookRepository;
    private final BookIssueRepository bookIssueRepository;
    private final UserRepository userRepository;

    public boolean issueRequest(String username, BookRequestDTO bookRequestDTO) {
        User user = userRepository.findByUsername(username).orElse(null);
        logger.debug("Query fired for finding the User by username");
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
        logger.debug("Saved an issue Request for a book for the username: "+username);
        return true;
    }


    public boolean cancelRequest(Long requestId) throws RequestNotFoundException {
        BookRequest request = bookRequestRepository.findById(requestId).orElse(null);
        logger.debug("Query fired for finding the book by Book Id");
        if (request == null) {
            throw new RequestNotFoundException("No request with request Id'" + requestId + "found");
        }
        if (request != null && request.getStatus().equals("PENDING")) {
            request.setStatus("CANCELLED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }

    public boolean acceptRequest(Long requestId) {
        BookRequest request = bookRequestRepository.findById(requestId).orElse(null);
        if (request == null) {
            throw new RequestNotFoundException("No request with request Id'" + requestId + "found");
        }

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
            System.out.println("Setting values for Book Issue Table done");
            bookIssueRepository.save(issue);//error
            System.out.println("Saved in Book Issue Repository");
            return true;
        }
        return false;
    }

    public boolean declineRequest(Long requestId) throws RequestNotFoundException{
        BookRequest request = bookRequestRepository.findById(requestId).orElse(null);
        if (request == null) {
            throw new RequestNotFoundException("No request with request Id'" + requestId + "found");
        }
        if (request != null && request.getStatus().equals("PENDING")) {
            request.setStatus("REJECTED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }
    public List<ViewRequestsDTO> viewRequest(String userName) throws RequestNotFoundException{
        List<BookRequest> bookRequests = bookRequestRepository.findAllRequestByUserId(userName);

        if (bookRequests.isEmpty()) {
            throw new RequestNotFoundException("No request by" + userName + "found");
        }
        List<ViewRequestsDTO> viewRequestsDTOList = new ArrayList<>();

        for (BookRequest bookRequest : bookRequests) {
            ViewRequestsDTO dto = new ViewRequestsDTO();
            dto.setRequestId(bookRequest.getRequestId());
            dto.setBookId(bookRequest.getBook().getBookId());
            dto.setAuthor(bookRequest.getBook().getAuthor());
            dto.setBookTitle(bookRequest.getBook().getTitle());
            dto.setPrintYear(bookRequest.getBook().getPrintYear());
            dto.setPublisherName(bookRequest.getBook().getPublisher().getName());
            dto.setRequestDate(bookRequest.getRequestDate());
            dto.setWeeksRequested(bookRequest.getWeeksRequested());
            dto.setStatus(bookRequest.getStatus());
            viewRequestsDTOList.add(dto);
        }

        return viewRequestsDTOList;

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

    public List<ManageBookRequestDTO> searchRequestsByUsername(String username) {
        List<BookRequest> requests = bookRequestRepository.findByUserUsername(username);
        return convertToDTOs(requests);
    }

    private List<ManageBookRequestDTO> converttoDTOs(List<BookRequest> bookRequests) {
        List<ManageBookRequestDTO> dtos = new ArrayList<>();
        for (BookRequest request : bookRequests) {
            ManageBookRequestDTO dto = new ManageBookRequestDTO();
            dto.setBookTitle(request.getBook().getTitle());
            dto.setRequestDate(request.getRequestDate());
            dtos.add(dto);
        }
        return dtos;
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
            dto.setRequestId(request.getRequestId()); // line added
            dto.setWeeksRequested(request.getWeeksRequested()); //line added
            dto.setStatus(request.getStatus()); //line added
            dtos.add(dto);
        }
        return dtos;
    }

    public List<ManageBookRequestDTO> searchRequestsByUsername(String username) {
        List<BookRequest> requests = bookRequestRepository.findByUserUsername(username);
        return convertToDTOs(requests);
    }

    private List<ManageBookRequestDTO> converttoDTOs(List<BookRequest> bookRequests) {
        List<ManageBookRequestDTO> dtos = new ArrayList<>();
        for (BookRequest request : bookRequests) {
            ManageBookRequestDTO dto = new ManageBookRequestDTO();
            dto.setBookTitle(request.getBook().getTitle());
            dto.setRequestDate(request.getRequestDate());
            dtos.add(dto);
        }
        return dtos;
    }

}

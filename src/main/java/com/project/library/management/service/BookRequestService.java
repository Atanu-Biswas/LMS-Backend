package com.project.library.management.service;

import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.dto.BookRequestPaginationDTO;
import com.project.library.management.dto.ManageBookRequestDTO;
import com.project.library.management.dto.ViewRequestsDTO;
import com.project.library.management.entity.*;
import com.project.library.management.exception.BookNotFoundException;
import com.project.library.management.exception.BookRequestNotFoundException;
import com.project.library.management.exception.DisabledBookException;
import com.project.library.management.exception.UserNotFoundException;
import com.project.library.management.repository.BookIssueRepository;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public boolean issueRequest(String username, BookRequestDTO bookRequestDTO) throws BookNotFoundException, DisabledBookException {
        User user = userRepository.findByUsername(username).orElse(null);
        logger.debug("Query fired for finding the User by username");
        Book book = bookRepository.findById(bookRequestDTO.getBookId()).orElseThrow(()->new BookNotFoundException("Requested Book Not Found!!"));

        if(!book.isActive()){
            throw new DisabledBookException("The book you are requesting is disabled!!");
        }

        BookRequest request = new BookRequest();
        request.setUser(user);
        request.setBook(book);
        request.setRequestDate(new Date());
        request.setWeeksRequested(bookRequestDTO.getWeeksRequested());
        bookRequestRepository.save(request);
        logger.debug("Saved an issue Request for a book for the username: " + username);
        return true;
    }

    public boolean cancelRequest(Long requestId) throws BookRequestNotFoundException {
        BookRequest request = bookRequestRepository.findById(requestId).orElse(null);
        logger.debug("Query fired for finding the book by Book Id");
        if (request == null) {
            throw new BookRequestNotFoundException("No request with request Id'" + requestId + "found");
        }
        if (request != null && request.getStatus().equals("PENDING")) {
            request.setStatus("CANCELLED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }

    public boolean acceptRequest(Long requestId) throws BookRequestNotFoundException, BookNotFoundException, DisabledBookException {
        BookRequest request = bookRequestRepository.findById(requestId).orElseThrow(()->new BookRequestNotFoundException("Book request with request Id: " +requestId+" not found"));


        Book book = bookRepository.findById(request.getBook().getBookId()).orElseThrow(()->new BookNotFoundException());

        if(!book.isActive()){
            throw new DisabledBookException("The requested book is disabled!!");
        }

        if (request != null && request.getStatus().equals("PENDING")) {
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
            issue.setFineStatus(Fine.valueOf("NOFINE"));
            issue.setUser(request.getUser());

            int bookQuantity = book.getQuantityAvailable();
            book.setQuantityAvailable(bookQuantity - 1);
            System.out.println("Setting values for Book Issue Table done");
            bookIssueRepository.save(issue);//error
            System.out.println("Saved in Book Issue Repository");

        }
        return true;
    }

    public boolean declineRequest(Long requestId) throws BookRequestNotFoundException {
        BookRequest request = bookRequestRepository.findById(requestId).orElseThrow(()->new BookRequestNotFoundException("Book request with request Id: " +requestId+" not found"));
        if (request != null && request.getStatus().equals("PENDING")) {
            request.setStatus("REJECTED");
            bookRequestRepository.save(request);
            return true;
        }
        return false;
    }

    public BookRequestPaginationDTO viewPendingRequests(int pageNumber, int pageSize, String sortBy, String sortDir) throws BookRequestNotFoundException {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<BookRequest> bookRequestPage = bookRequestRepository.findAllPendingRequests(p);
        if(bookRequestPage.isEmpty()){
            throw new BookRequestNotFoundException("No request is pending!!");
        }

        List<BookRequest> bookRequests = bookRequestPage.getContent();

        List<BookRequestDTO> bookRequestDTOs = bookRequests.stream().map(bookRequest -> BookRequestDTO.builder()
                .bookId(bookRequest.getBook().getBookId())
                .bookTitle(bookRequest.getBook().getTitle())
                .weeksRequested(bookRequest.getWeeksRequested())
                .status(bookRequest.getStatus())
                .requestId(bookRequest.getRequestId())
                .username(bookRequest.getUser().getUsername())
                .requestDate(bookRequest.getRequestDate())
                .quantityAvailable(bookRequest.getBook().getQuantityAvailable())
                .build())
                .toList();

        return BookRequestPaginationDTO.builder()
                .content(bookRequestDTOs)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(bookRequestPage.getTotalElements())
                .totalPages(bookRequestPage.getTotalPages())
                .isLastPage(bookRequestPage.isLast())
                .build();
    }

    public List<ViewRequestsDTO> viewRequest(String userName) throws BookRequestNotFoundException {
        List<BookRequest> bookRequests = bookRequestRepository.findAllRequestByUserId(userName);

        if (bookRequests.isEmpty()) {
            throw new BookRequestNotFoundException("No request by" + userName + "found");
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

    public List<ManageBookRequestDTO> getLast10BookRequests() throws BookRequestNotFoundException {
        List<BookRequest> last10Requests = bookRequestRepository.findTop10ByOrderByRequestDateDesc();
        if(last10Requests.isEmpty()){
            throw new BookRequestNotFoundException();
        }
        return convertToDTOs(last10Requests);
    }

//    private List<ManageBookRequestDTO> convertToDTOs(List<BookRequest> bookRequests) {
//        List<ManageBookRequestDTO> dtos = new ArrayList<>();
//        for (BookRequest request : bookRequests) {
//            ManageBookRequestDTO dto = new ManageBookRequestDTO();
//            dto.setUsername(request.getUser().getUsername());
//            dto.setBookTitle(request.getBook().getTitle());
//            dto.setRequestDate(request.getRequestDate());
//            dtos.add(dto);
//        }
//        return dtos;
//    }

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

//    public List<ManageBookRequestDTO> getLast10BookRequests() {
//        List<BookRequest> last10Requests = bookRequestRepository.findTop10ByOrderByRequestDateDesc();
//        return convertToDTOs(last10Requests);
//    }

    private List<ManageBookRequestDTO> convertToDTOs(List<BookRequest> bookRequests) {
        List<ManageBookRequestDTO> dtos = new ArrayList<>();
        for (BookRequest request : bookRequests) {
            ManageBookRequestDTO dto = new ManageBookRequestDTO();
            dto.setUsername(request.getUser().getUsername());
            dto.setBookTitle(request.getBook().getTitle());
            dto.setRequestDate(request.getRequestDate());
            dto.setRequestId(request.getRequestId()); // line added
            dto.setWeeksRequested(request.getWeeksRequested()); // line added
            dto.setStatus(request.getStatus()); // line added
            dtos.add(dto);
        }
        return dtos;
    }

    // add this service
    public BookRequestPaginationDTO viewPendingRequestsByUserName(int pageNumber, int pageSize, String sortBy,
            String sortDir, String username) throws BookRequestNotFoundException, UserNotFoundException {

        boolean isUsernameValid = userRepository.existsByUsername(username);
        if (!isUsernameValid) {
            throw new UserNotFoundException("User with username '" + username + "' does not exist!!");
        }

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        Page<BookRequest> bookRequestPage = bookRequestRepository.findAllPendingRequestsByUserName(username, p);
        logger.debug("Query fired for finding the Pending Requests by username");
        if (bookRequestPage.isEmpty()) {
            logger.error("BookRequestNotFound Exception generated !");
            throw new BookRequestNotFoundException("No pending requests foung for user with given username!!");
        }

        List<BookRequest> bookRequests = bookRequestPage.getContent();

        List<BookRequestDTO> bookRequestDTOs = bookRequests.stream().map(bookRequest -> BookRequestDTO.builder()
                .bookId(bookRequest.getBook().getBookId())
                .bookTitle(bookRequest.getBook().getTitle())
                .weeksRequested(bookRequest.getWeeksRequested())
                .status(bookRequest.getStatus())
                .requestId(bookRequest.getRequestId())
                .username(bookRequest.getUser().getUsername())
                .requestDate(bookRequest.getRequestDate())
                .quantityAvailable(bookRequest.getBook().getQuantityAvailable())
                .build())
                .toList();

        return BookRequestPaginationDTO.builder()
                .content(bookRequestDTOs)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(bookRequestPage.getTotalElements())
                .totalPages(bookRequestPage.getTotalPages())
                .isLastPage(bookRequestPage.isLast())
                .build();
    }
    // TODO Auto-generated method stub

}

//    public List<ManageBookRequestDTO> searchRequestsByUsername(String username) {
//        List<BookRequest> requests = bookRequestRepository.findByUserUsername(username);
//        return convertToDTOs(requests);
//    }

//    private List<ManageBookRequestDTO> converttoDTOs(List<BookRequest> bookRequests) {
//        List<ManageBookRequestDTO> dtos = new ArrayList<>();
//        for (BookRequest request : bookRequests) {
//            ManageBookRequestDTO dto = new ManageBookRequestDTO();
//            dto.setBookTitle(request.getBook().getTitle());
//            dto.setRequestDate(request.getRequestDate());
//            dtos.add(dto);
//        }
//        return dtos;
//    }



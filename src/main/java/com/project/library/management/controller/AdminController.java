package com.project.library.management.controller;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.dto.BookPaginationDTO;
import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.exception.PublisherNotFoundException;
import com.project.library.management.exception.RequestNotFoundException;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
//@RequiredArgsConstructor
public class AdminController {
    private final BookRequestService bookRequestService;
    private final BookIssueService bookIssueService;
    private final BookRequestRepository bookRequestRepository;
    private final BookService bookService;
    private final UserService userService;

    private static final Logger logger = Logger.getLogger(AdminController.class);

    private final PublisherService publisherService;

    @GetMapping("/test")
    protected ResponseEntity<String> test() {
        logger.debug("Debug message");
        logger.warn("Warning message");
        logger.info("Info message");
        return ResponseEntity.ok("Hello from admin only url");
    }

    @GetMapping("/accept-request")
    // working //edited for front-end
    protected ResponseEntity<Boolean> acceptRequest(@Param("requestId") Long requestId) throws RequestNotFoundException {
        boolean isRequestAccepted = bookRequestService.acceptRequest(requestId);
        return ResponseEntity.ok(isRequestAccepted);
    }

    @GetMapping("/decline-request")
    // working // edited for front-end
    protected ResponseEntity<Boolean> declineRequest(@Param("requestId") Long requestId) throws RequestNotFoundException {
        boolean isRequestDeclined = bookRequestService.declineRequest(requestId);
        return ResponseEntity.ok(isRequestDeclined);
    }

    @PostMapping("/addBook")
    // working
    protected ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) throws PublisherNotFoundException {
        System.out.println("bookDTO: " + bookDTO);
        bookService.addBook(bookDTO);
        return ResponseEntity.ok("book is added successfully");
    }

    @GetMapping("/getBookByName/{name}" )
    //working
    protected ResponseEntity<BookDTO> getBookByName(@PathVariable("name") String name){
       BookDTO book=bookService.getBookByName(name);
       return ResponseEntity.of(Optional.of(book));
    }

    @GetMapping("/pending-requests")
    public List<BookRequestDTO> getPendingRequests() {
        return bookRequestService.viewPendingRequests();
    }

    @GetMapping("/view-all-books")
    protected ResponseEntity<BookPaginationDTO> getAllBooks(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("sortBy") String sortBy, @Param("sortDir") String sortDir ){

        BookPaginationDTO bookPaginationDTO= bookService.getAllBooks(pageNumber,pageSize,sortBy,sortDir);

        return ResponseEntity.of(Optional.of(bookPaginationDTO));
    }

}

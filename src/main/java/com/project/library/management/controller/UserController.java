 package com.project.library.management.controller;

import com.project.library.management.dto.*;
import com.project.library.management.exception.*;
import com.project.library.management.repository.BookIssueRepository;
import com.project.library.management.service.BookIssueService;
import com.project.library.management.service.BookRequestService;
import com.project.library.management.service.BookService;
import com.project.library.management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;
    private final BookRequestService bookRequestService;
    private final BookService bookService;
    private final BookIssueService bookIssueService;

    @GetMapping("/test")
    protected ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from user only url");
    }
    @GetMapping("/is-username-available")
    //working
    protected ResponseEntity<Boolean> isUsernameAvailable(@Param("username") String username) {
        logger.debug("Debug message");
        return ResponseEntity.ok(userService.isUsernameAvailable(username));
    }
    @PostMapping("/request-book")
    //working //changed for frontend
    public ResponseEntity<Boolean> issueBookRequest(@RequestBody BookRequestDTO bookRequestDTO, HttpServletRequest request) throws BookNotFoundException, DisabledBookException, BookAlreadyRequestedExecption {
        String username = request.getUserPrincipal().getName();
        System.out.println("username: "+username);
        boolean isIssueSuccessful = bookRequestService.issueRequest(username, bookRequestDTO);
        System.out.println(isIssueSuccessful);
        return ResponseEntity.ok(isIssueSuccessful);
    }
    @GetMapping("/cancel-request")
    //working
    public ResponseEntity<Boolean> declineBookRequest(@Param("requestId") Long requestId) throws BookRequestNotFoundException{
        boolean isRequestCancelled = bookRequestService.cancelRequest(requestId);
        return ResponseEntity.ok(isRequestCancelled);

    }
    @GetMapping("/view-request")
    public ResponseEntity<List<ViewRequestsDTO>> viewRequests(HttpServletRequest request) throws BookRequestNotFoundException{
        String userName=request.getUserPrincipal().getName();
         List<ViewRequestsDTO> viewRequestDTOs=bookRequestService.viewRequest(userName);

        return ResponseEntity.ok(viewRequestDTOs);
    }

    @GetMapping("/view-borrowed-books")
    protected ResponseEntity<BookIssuePaginationDTO> viewBorrowedBooks(HttpServletRequest request,
                                                                         @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
                                                                         @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
                                                                         @RequestParam(value = "sortBy", defaultValue ="issueDate", required = false) String sortBy,
                                                                         @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ) throws BookIssueNotFoundException, UserNotFoundException {
        String username = request.getUserPrincipal().getName();
        BookIssuePaginationDTO borrowedBooks = bookIssueService.viewBorrowedBooks(username,pageNumber,pageSize,sortBy,sortDir);
        return ResponseEntity.ofNullable(borrowedBooks);
    }


    @GetMapping("/get-book-List-by-title" )
    protected ResponseEntity<BookPaginationDTO> getBookByTitle (
        @RequestParam("title") String title,
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="title", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir) throws BookNotFoundException {

        BookPaginationDTO bookPaginationDTO=bookService.getBookListByTitle(title,pageNumber,pageSize,sortBy,sortDir);
        return ResponseEntity.of(Optional.of(bookPaginationDTO));
    }
     //keep these changes
     @GetMapping("/get-book-by-id" )
    protected ResponseEntity<BookDTO> getBookById (@Param("id") Long id) throws BookNotFoundException {
       BookDTO book=bookService.getBookById(id);

       return ResponseEntity.of(Optional.of(book));
    }




    @GetMapping("/get-book-by-authorName" )
    protected ResponseEntity<BookPaginationDTO> getBooksByAuthorName (
        @RequestParam("author") String author,
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="title", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir) throws BookNotFoundException{

        BookPaginationDTO bookPaginationDTO=bookService.getBooksByAuthor(author,pageNumber,pageSize,sortBy,sortDir);

       return ResponseEntity.of(Optional.of(bookPaginationDTO));
    }

    @Autowired
    private BookIssueRepository booksIssuedRepository;

    @GetMapping("/fines")
    public ResponseEntity<FinePaginationDTO> getUserFines(HttpServletRequest request,
    @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
    @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
    @RequestParam(value = "sortBy", defaultValue ="returnDate", required = false) String sortBy,
    @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir) throws NoFineFoundException {
        String username = request.getUserPrincipal().getName();

        FinePaginationDTO finePaginationDTO = userService.viewFine(pageNumber, pageSize, sortBy, sortDir,username);

        return ResponseEntity.ok(finePaginationDTO);
    }

    @GetMapping("/view-all-books")
    protected ResponseEntity<BookPaginationDTO> getAllBooks(
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="title", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ) throws BookNotFoundException{

        BookPaginationDTO bookPaginationDTO = bookService.getAllBooks(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.of(Optional.of(bookPaginationDTO));
    }
 ///keep this
    @GetMapping("/view-requests")
    public ResponseEntity<BookRequestPaginationDTO> getPendingRequestsByUser(HttpServletRequest request,
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="requestDate", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir,
        @RequestParam(value = "filterBy",required = false) String filterBy) throws BookRequestNotFoundException, UserNotFoundException {

        String username = request.getUserPrincipal().getName();
        BookRequestPaginationDTO bookRequestPaginationDTO=bookRequestService.viewRequestsByUserName(pageNumber, pageSize, sortBy, sortDir,username,filterBy);

        return ResponseEntity.of(Optional.of(bookRequestPaginationDTO));
    }
}



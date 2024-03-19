package com.project.library.management.controller;

import com.project.library.management.dto.*;
import com.project.library.management.entity.BookIssue;
import com.project.library.management.exception.AuthorNotFoundException;
import com.project.library.management.exception.BookNotFoundException;
import com.project.library.management.exception.RequestNotFoundException;
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
    public ResponseEntity<Boolean> issueBookRequest(@RequestBody BookRequestDTO bookRequestDTO, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        System.out.println("username: "+username);
        boolean isIssueSuccessful = bookRequestService.issueRequest(username, bookRequestDTO);
        System.out.println(isIssueSuccessful);
        return ResponseEntity.ok(isIssueSuccessful);
    }
    @GetMapping("/cancel-request")
    //working
    public ResponseEntity<String> declineBookRequest(@Param("requestId") Long requestId) throws RequestNotFoundException{
        boolean isRequestCancelled = bookRequestService.cancelRequest(requestId);
        return ResponseEntity.ok(isRequestCancelled ? "Book Request Cancelled!!!" : "Book Request Retained!!!");

    }
    @GetMapping("/view-request")
    public ResponseEntity<List<ViewRequestsDTO>> viewRequests(HttpServletRequest request) throws RequestNotFoundException{
        String userName=request.getUserPrincipal().getName();
         List<ViewRequestsDTO> viewRequestDTOs=bookRequestService.viewRequest(userName);

        return ResponseEntity.ok(viewRequestDTOs);
    }

    @GetMapping("/view-borrowed-books")
    protected ResponseEntity<List<BookIssueDTO>> viewBorrowedBooks(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        List<BookIssueDTO> borrowedBooks = bookIssueService.viewBorrowedBooks(username);
        return ResponseEntity.ofNullable(borrowedBooks);
    }
     //remone this !!!!!//
     @GetMapping("/get-book-by-name" )
    protected ResponseEntity<BookDTO> getBookByName (@Param("name") String name) throws BookNotFoundException {
       BookDTO book=bookService.getBookByName(name);

       return ResponseEntity.of(Optional.of(book));
    }

    @GetMapping("/get-book-List-by-title" )
    protected ResponseEntity<List<BookDTO>> getBookByTitle (@Param("title") String title) throws BookNotFoundException {
        List<BookDTO> bookDTOs=bookService.getBookListByTitle(title);
        return ResponseEntity.ok(bookDTOs);
    }
     //keep these changes
     @GetMapping("/get-book-by-id" )
    protected ResponseEntity<BookDTO> getBookById (@Param("id") Long id) throws BookNotFoundException {
       BookDTO book=bookService.getBookById(id);

       return ResponseEntity.of(Optional.of(book));
    }




    @GetMapping("/get-book-by-authorName" )
    protected ResponseEntity<List<BookDTO>> getBooksByAuthorName (@Param("author") String author) throws AuthorNotFoundException {
      List <BookDTO> book=bookService.getBooksByAuthor(author);

       return ResponseEntity.ok(book);
    }

    @Autowired
    private BookIssueRepository booksIssuedRepository;

    //keepThe changes
    @GetMapping("/fines")
    public ResponseEntity<FinePaginationDTO> getUserFines(HttpServletRequest request,
    @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
    @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
    @RequestParam(value = "sortBy", defaultValue ="returnDate", required = false) String sortBy,
    @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir) {
        String username = request.getUserPrincipal().getName();
    
        FinePaginationDTO finePaginationDTO = userService.viewFine(pageNumber, pageSize, sortBy, sortDir,username);
       
        return ResponseEntity.ok(finePaginationDTO);
    }

    @GetMapping("/view-all-books")
    protected ResponseEntity<BookPaginationDTO> getAllBooks(
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="title", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ){

        BookPaginationDTO bookPaginationDTO = bookService.getAllBooks(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.of(Optional.of(bookPaginationDTO));
    }
 ///keep this
    @GetMapping("/pending-requests")
    public ResponseEntity<BookRequestPaginationDTO> getPendingRequestsByUser(HttpServletRequest request,
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="requestDate", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir) {
 
            String username = request.getUserPrincipal().getName();
        BookRequestPaginationDTO bookRequestPaginationDTO=bookRequestService.viewPendingRequestsByUserName(pageNumber, pageSize, sortBy, sortDir,username);
 
        return ResponseEntity.of(Optional.of(bookRequestPaginationDTO));
    }
}



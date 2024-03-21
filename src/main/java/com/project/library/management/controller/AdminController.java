package com.project.library.management.controller;

import com.project.library.management.dto.*;
import com.project.library.management.exception.BookIdNotFoundException;
import com.project.library.management.exception.BookNotDeletedException;
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


    @GetMapping("/pending-requests")
    public ResponseEntity<BookRequestPaginationDTO> getPendingRequests(
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="requestDate", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ) {

        BookRequestPaginationDTO bookRequestPaginationDTO=bookRequestService.viewPendingRequests(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.of(Optional.of(bookRequestPaginationDTO));
    }

    @GetMapping("/view-all-books")
    protected ResponseEntity<BookPaginationDTO> getAllBooks(
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="title", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ){

        BookPaginationDTO bookPaginationDTO= bookService.getAllBooks(pageNumber,pageSize,sortBy,sortDir);

        return ResponseEntity.of(Optional.of(bookPaginationDTO));
    }

    @GetMapping("/view-all-users")
    protected ResponseEntity<UserPaginationDTO> getAllUsers(
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="username", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ){

        UserPaginationDTO userPaginationDTO= userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);

        return ResponseEntity.of(Optional.of(userPaginationDTO));
    }


        @GetMapping("/enable-disable")
    //WORKING // changed for frontend
    protected ResponseEntity<Boolean> enableDisableBook(@RequestParam(value = "id") Long id) throws BookIdNotFoundException {
        System.out.println("Inside Controller"+id);
        boolean isBookDisabled=bookService.enableDisableBook(id);
        System.out.println("Book status: "+isBookDisabled);
        return ResponseEntity.ok(isBookDisabled);
    }

        @GetMapping("/last-10-book-requests")
    public ResponseEntity<List<ManageBookRequestDTO>> getLast10BookRequests() {
        List<ManageBookRequestDTO> last10Requests = bookRequestService.getLast10BookRequests();
        return ResponseEntity.ok(last10Requests);
    }


   //view pending request by username keeep this also
   @GetMapping("/pending-requests-by-username")
   public ResponseEntity<BookRequestPaginationDTO> getPendingRequestsByUser(
       @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
       @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
       @RequestParam(value = "sortBy", defaultValue ="requestDate", required = false) String sortBy,
       @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ,
       @RequestParam String username) {
       BookRequestPaginationDTO bookRequestPaginationDTO=bookRequestService.viewPendingRequestsByUserName(pageNumber, pageSize, sortBy, sortDir,username);

       return ResponseEntity.of(Optional.of(bookRequestPaginationDTO));
   }



    @GetMapping("/view-borrowed-books")
    protected ResponseEntity<BookIssuePaginationDTO> viewBorrowedBooks(@RequestParam("username") String username,
                                                                       @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
                                                                       @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue ="issueDate", required = false) String sortBy,
                                                                       @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ) {

        BookIssuePaginationDTO borrowedBooks = bookIssueService.viewBorrowedBooks(username,pageNumber,pageSize,sortBy,sortDir);
        return ResponseEntity.ofNullable(borrowedBooks);
    }


 //keep this also
   @GetMapping("/view-user-by-username")
   protected ResponseEntity<UserPaginationDTO> getUserByUsername(
    @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
   @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
   @RequestParam(value = "sortBy", defaultValue ="name", required = false) String sortBy,
   @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ,
   String username ){

    UserPaginationDTO userDTOs= userService.getUserByUsername(pageNumber, pageSize, sortBy, sortDir,username);

       return ResponseEntity.ok(userDTOs);
   }

    @GetMapping("/accept-return")
    protected ResponseEntity<String> acceptReturn(@RequestParam("issueId") Long issueId){
        System.out.println("issueId: "+issueId);
        boolean isReturnAccepted = bookIssueService.acceptReturn(issueId);

        System.out.println("inside accept return");

        return ResponseEntity.ok(isReturnAccepted ? "Book return is successfully accepted" : "Book return is unsuccessful!!!");
    }

 //keep this
 @DeleteMapping("/delete-book")
 protected ResponseEntity<Boolean> deleteBook(@RequestParam("bookId") Long bookId )throws BookNotDeletedException{

    boolean isDeleted=bookService.deleteBook(bookId);
    System.out.println(isDeleted);
    return ResponseEntity.ok(isDeleted);
 }

}

package com.project.library.management.controller;

import com.project.library.management.dto.*;
import com.project.library.management.exception.*;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.service.*;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
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
    private final AdminDashboardService adminDashboardService;
    private final ReportService reportService;


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
    protected ResponseEntity<Boolean> acceptRequest(@Param("requestId") Long requestId) throws BookRequestNotFoundException, BookNotFoundException, DisabledBookException {
        boolean isRequestAccepted = bookRequestService.acceptRequest(requestId);
        return ResponseEntity.ok(isRequestAccepted);
    }

    @GetMapping("/decline-request")
    // working // edited for front-end
    protected ResponseEntity<Boolean> declineRequest(@Param("requestId") Long requestId) throws BookRequestNotFoundException {
        boolean isRequestDeclined = bookRequestService.declineRequest(requestId);
        return ResponseEntity.ok(isRequestDeclined);
    }

    @PostMapping("/add-book")
    // working
    protected ResponseEntity<Boolean> addBook(@RequestBody BookDTO bookDTO) throws PublisherNotFoundException, IsbnLengthException, IllegalQuantityException, IllegalPrintYear {
        System.out.println("bookDTO: " + bookDTO);
        bookService.addBook(bookDTO);
        Boolean isBookAdded = true;
        return ResponseEntity.ok(isBookAdded);
    }


    @GetMapping("/view-requests")
    public ResponseEntity<BookRequestPaginationDTO> getRequests(
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="requestDate", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir,
        @RequestParam(value = "filterBy",required = false) String filterBy) throws BookRequestNotFoundException {

        BookRequestPaginationDTO bookRequestPaginationDTO=bookRequestService.viewRequests(pageNumber, pageSize, sortBy, sortDir,filterBy);

        return ResponseEntity.of(Optional.of(bookRequestPaginationDTO));
    }

    @GetMapping("/view-all-books")
    protected ResponseEntity<BookPaginationDTO> getAllBooks(
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="title", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ) throws BookNotFoundException{

        BookPaginationDTO bookPaginationDTO= bookService.getAllBooks(pageNumber,pageSize,sortBy,sortDir);

        return ResponseEntity.of(Optional.of(bookPaginationDTO));
    }

    @GetMapping("/view-all-users")
    protected ResponseEntity<UserPaginationDTO> getAllUsers(
        @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue ="username", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ) throws UserNotFoundException{

        UserPaginationDTO userPaginationDTO= userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);

        return ResponseEntity.of(Optional.of(userPaginationDTO));
    }


        @GetMapping("/enable-disable")
    //WORKING // changed for frontend
    protected ResponseEntity<Boolean> enableDisableBook(@RequestParam(value = "id") Long id) throws BookNotFoundException {
        System.out.println("Inside Controller"+id);
        boolean isBookDisabled=bookService.enableDisableBook(id);
        System.out.println("Book status: "+isBookDisabled);
        return ResponseEntity.ok(isBookDisabled);
    }

        @GetMapping("/last-10-book-requests")
    public ResponseEntity<List<ManageBookRequestDTO>> getLast10BookRequests() throws BookRequestNotFoundException {
        List<ManageBookRequestDTO> last10Requests = bookRequestService.getLast10BookRequests();
        return ResponseEntity.ok(last10Requests);
    }


   @GetMapping("/view-requests-by-username")
   public ResponseEntity<BookRequestPaginationDTO> getRequestsByUsername(
       @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
       @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
       @RequestParam(value = "sortBy", defaultValue ="requestDate", required = false) String sortBy,
       @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ,
       @RequestParam("username") String username, 
       @RequestParam(value = "filterBy",required = false) String filterBy) throws BookRequestNotFoundException, UserNotFoundException {

       BookRequestPaginationDTO bookRequestPaginationDTO=bookRequestService.viewRequestsByUserName(pageNumber, pageSize, sortBy, sortDir,username,filterBy);

       return ResponseEntity.of(Optional.of(bookRequestPaginationDTO));
   }



    @GetMapping("/view-borrowed-books")
    protected ResponseEntity<BookIssuePaginationDTO> viewBorrowedBooks(@RequestParam("username") String username,
                                                                       @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
                                                                       @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue ="issueDate", required = false) String sortBy,
                                                                       @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir ) throws BookIssueNotFoundException, UserNotFoundException {

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
   @RequestParam("username") String username ) throws UserNotFoundException{

    UserPaginationDTO userPaginationDTO= userService.getUserByUsername(pageNumber, pageSize, sortBy, sortDir,username);

       return ResponseEntity.ok(userPaginationDTO);
   }

    @GetMapping("/accept-return")
    protected ResponseEntity<Boolean> acceptReturn(@RequestParam("issueId") Long issueId) throws BookIssueNotFoundException {
        System.out.println("issueId: "+issueId);
        boolean isReturnAccepted = bookIssueService.acceptReturn(issueId);

        System.out.println("inside accept return");

        return ResponseEntity.ok(isReturnAccepted);
    }

    @GetMapping("/fines")
    public ResponseEntity<FinePaginationDTO> getUserFines(@RequestParam("username") String username,
                                                          @RequestParam(value = "pageNumber", defaultValue ="0", required = false) int pageNumber,
                                                          @RequestParam(value = "pageSize", defaultValue ="10", required = false) int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue ="returnDate", required = false) String sortBy,
                                                          @RequestParam(value = "sortDir", defaultValue ="asc", required = false) String sortDir) throws NoFineFoundException {


        FinePaginationDTO finePaginationDTO = userService.viewFine(pageNumber, pageSize, sortBy, sortDir,username);

        return ResponseEntity.ok(finePaginationDTO);
    }

 //keep this
 @DeleteMapping("/delete-book")
 protected ResponseEntity<Boolean> deleteBook(@RequestParam("bookId") Long bookId )throws BookNotDeletedException{

    boolean isDeleted=bookService.deleteBook(bookId);
    System.out.println(isDeleted);
    return ResponseEntity.ok(isDeleted);
 }

   @GetMapping("/admin-dashboard")
    public ResponseEntity<AdminDashboardDTO> adminDashboard() {

       return ResponseEntity.ok(AdminDashboardDTO.builder()
               .genreWiseSplit(adminDashboardService.getGenreWiseSplit())
               .mostRequestedBooks(adminDashboardService.calculateAdminDashboardData())
               .totalBooksAvailable(adminDashboardService.getTotalBooksAvailable())
               .totalUsers(adminDashboardService.getTotalUsersAvailable())
               .TopAuthorsWithBooksIssued(adminDashboardService.getTopAuthorsWithBooksIssued(5))
               .totalFines(adminDashboardService.getTotalFinesPaidVersusDue())
               .build());
    }



    @GetMapping("generate-report")
    protected ResponseEntity<String> generateReport(@RequestParam("reportFormat") String reportFormat) throws FileNotFoundException, JRException{
        String generatedReport=reportService.generateBooksCollectionReport(reportFormat);
        return ResponseEntity.ok(generatedReport);
    }


}

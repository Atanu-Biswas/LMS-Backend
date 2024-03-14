package com.project.library.management.controller;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.dto.FineDTO;
import com.project.library.management.entity.BookIssue;
import com.project.library.management.exception.AuthorNotFoundException;
import com.project.library.management.exception.BookNotFoundException;
import com.project.library.management.repository.BookIssueRepository;
import com.project.library.management.service.BookIssueService;
import com.project.library.management.service.BookRequestService;
import com.project.library.management.service.BookService;
import com.project.library.management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
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
        return ResponseEntity.ok(userService.isUsernameAvailable(username));
    }
    @PostMapping("/request-book")
    //working
    public ResponseEntity<String> issueBookRequest(@RequestBody BookRequestDTO bookRequestDTO, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        System.out.println("username: "+username);
        boolean isIssueSuccessful = bookRequestService.issueRequest(username, bookRequestDTO);
        return ResponseEntity.ok(isIssueSuccessful ? "Book Requested Successfully!!!" : "Book Request Unsuccessfull!!!");
    }
    @GetMapping("/cancel-request")
    //working
    public ResponseEntity<String> declineBookRequest(@Param("requestId") Long requestId){
        boolean isRequestCancelled = bookRequestService.cancelRequest(requestId);
        return ResponseEntity.ok(isRequestCancelled ? "Book Request Cancelled!!!" : "Book Request Retained!!!");

    }

//    @GetMapping("/view-borrowed-books")
//    protected ResponseEntity<List<BookIssueDTO>> viewBorrowedBooks(HttpServletRequest request) {
//        String username = request.getUserPrincipal().getName();
//        List<BookIssueDTO> borrowedBooks = bookIssueService.viewBorrowedBooks(username);
//        return ResponseEntity.ofNullable(borrowedBooks);
//    }

     @GetMapping("/getBookByName" )
    protected ResponseEntity<BookDTO> getBookByName (@Param("name") String name) throws BookNotFoundException {
       BookDTO book=bookService.getBookByName(name);
       return ResponseEntity.of(Optional.of(book));}
     @GetMapping("/get-book-by-title" )
    protected ResponseEntity<List<BookDTO>> getBookByTitle (@Param("title") String title) throws BookNotFoundException {
        List<BookDTO> bookDTOs=bookService.getBookListByTitle(title);
       return ResponseEntity.ok(bookDTOs);
    }




    @GetMapping("/get-book-by-authorName" )
    protected ResponseEntity<List<BookDTO>> getBooksByAuthorName (@Param("author") String author) throws AuthorNotFoundException {
      List <BookDTO> book=bookService.getBooksByAuthor(author);

       return ResponseEntity.ok(book);
    }

    @Autowired
    private BookIssueRepository booksIssuedRepository;

    @GetMapping("/fines")
    public ResponseEntity<List<FineDTO>> getUserFines(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        List<BookIssue> finedBookIssues = booksIssuedRepository.calculateFinesByUserName(username);
        List<FineDTO> fineDTOList = finedBookIssues.stream()
                .map((BookIssue bookIssue)->
                        FineDTO.
                        builder().
                                issueId(bookIssue.getIssueId()).
                                fine(bookIssue.getFine()).
                                bookName(bookIssue.getBook().getTitle())
                                .build()
                ).toList();
        return ResponseEntity.ok(fineDTOList);
    }
}



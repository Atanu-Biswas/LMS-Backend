package com.project.library.management.controller;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.service.BookRequestService;
import com.project.library.management.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BookRequestService bookRequestService;
    private final BookService bookService;


    @GetMapping("/test")
    protected ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from admin only url");
    }
    @GetMapping("/accept-request")
    protected ResponseEntity<String> acceptRequest(@Param("requestId") Long requestId) {
        boolean isRequestAccepted = bookRequestService.acceptRequest(requestId);
        return ResponseEntity.ok(isRequestAccepted ? "Book Request Accepted!!!" : "Book Request still Pending!!!");
    }
    @GetMapping("/decline-request")
    protected ResponseEntity<String> declineRequest(@Param("requestId") Long requestId) {
        boolean isRequestDeclined = bookRequestService.declineRequest(requestId);
        return ResponseEntity.ok(isRequestDeclined ? "Book Request Rejected!!!" : "Book Request still Pending!!!");
    }




    @PostMapping("/addBook")
    protected ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) throws PublisherNotFoundException {
     System.out.println("bookDTO: "+bookDTO);
        bookService.addBook(bookDTO);
      return ResponseEntity.ok("book is added successfully");
    }

    @GetMapping("/getBookByName/{name}" )
    protected ResponseEntity<BookDTO> getBookByName(@PathVariable("name") String name){
       BookDTO book=bookService.getBookByName(name);
       return ResponseEntity.of(Optional.of(book));
    }

}

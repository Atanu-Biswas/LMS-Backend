package com.project.library.management.controller;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.dto.PublisherDTO;
import com.project.library.management.exception.AuthorNotFoundException;
import com.project.library.management.exception.BookIdNotFoundException;
import com.project.library.management.exception.BookNotFoundException;
import com.project.library.management.exception.PublisherNotFoundException;
import com.project.library.management.service.BookRequestService;
import com.project.library.management.service.BookService;
import com.project.library.management.service.PublisherService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/admin")
//@RequiredArgsConstructor
public class AdminController {
    private final BookRequestService bookRequestService;
    private final BookService bookService;


    private final PublisherService publisherService;

    @GetMapping("/test")
    protected ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from admin only url");
    }

    @GetMapping("/accept-request")
    // working
    protected ResponseEntity<String> acceptRequest(@Param("requestId") Long requestId) {
        boolean isRequestAccepted = bookRequestService.acceptRequest(requestId);
        return ResponseEntity.ok(isRequestAccepted ? "Book Request Accepted!!!" : "Book Request still Pending!!!");
    }

    @GetMapping("/decline-request")
    // working
    protected ResponseEntity<String> declineRequest(@Param("requestId") Long requestId) {
        boolean isRequestDeclined = bookRequestService.declineRequest(requestId);
        return ResponseEntity.ok(isRequestDeclined ? "Book Request Rejected!!!" : "Book Request still Pending!!!");
    }

    @PostMapping("/add-book")
    // working
    protected ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) throws PublisherNotFoundException {
        System.out.println("bookDTO: " + bookDTO);
        bookService.addBook(bookDTO);
        return ResponseEntity.ok("book is added successfully");
    }

    @PostMapping("/enable-disable-book")
    //WORKING
    protected ResponseEntity<String> enableDisableBook(@Param("id") Long id) throws BookIdNotFoundException {
        boolean isBookDisabled=bookService.enableDisableBook(id);
        return ResponseEntity.ok(isBookDisabled ?"Book Is Disabled" :"Book Is Enabled");
    }



    @PostMapping("/publisher")
//    working
    protected ResponseEntity<String> addPublisher(@RequestBody PublisherDTO publisherDTO){

        publisherService.addPublisher(publisherDTO);

        return ResponseEntity.ok("Publisher is added Successfully!");
    }

    @GetMapping("/publisher")
//    working
    protected ResponseEntity<PublisherDTO> getPublisher(@Param("id") Long id){

        PublisherDTO publisherDTO = publisherService.getPublisher(id);

        return ResponseEntity.of(Optional.of(publisherDTO));
    }

    @PutMapping("/publisher")
    //working
    protected ResponseEntity<String> updatePublisher(@RequestBody PublisherDTO publisherDTO, @Param("id") Long id){

        publisherService.updatePublisher(publisherDTO,id);

        return ResponseEntity.ok("Publisher is updated successfully!");
    }

    @DeleteMapping("/publisher")
    //working
    protected ResponseEntity<String> deletePublisher(@Param("publisherId") Long publisherId){
        System.out.println("Id "+publisherId);
        publisherService.deletePublisher(publisherId);

        return ResponseEntity.ok("Publisher with id "+publisherId+" is deleted successfully!");
    }
     @GetMapping("/get-book-by-title" )
    protected ResponseEntity<List<BookDTO>> getBookByName (@Param("title") String title) throws BookNotFoundException {
        List<BookDTO> bookDTOs=bookService.getBookByTitle(title);
        return ResponseEntity.ok(bookDTOs);
    }
    
    @GetMapping("/get-book-by-authorName" )
    protected ResponseEntity<List<BookDTO>> getBooksByAuthorName (@Param("authorName") String authorName) throws AuthorNotFoundException {
      List <BookDTO> bookDTOs=bookService.getBooksByAuthor(authorName);
     
       return ResponseEntity.ok(bookDTOs);
    }
    @GetMapping("/get-book-by-publisherName" )
    protected ResponseEntity<List<BookDTO>> getBooksByPublisherName (@Param("publisherName") String publisherName) throws AuthorNotFoundException {
      List <BookDTO> book=bookService.getBooksByPublisher(publisherName);
     
       return ResponseEntity.ok(book);
    }
}

package com.project.library.management.controller;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.dto.PublisherDTO;
import com.project.library.management.exception.BookIdNotFoundException;
import com.project.library.management.exception.PublisherNotFoundException;
import com.project.library.management.service.BookRequestService;
import com.project.library.management.service.BookService;
import com.project.library.management.service.PublisherService;
import lombok.AllArgsConstructor;
import java.util.List;


import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.library.management.dto.ManageBookRequestDTO;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
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
    // working //edited for front-end
    protected ResponseEntity<Boolean> acceptRequest(@Param("requestId") Long requestId) {
        boolean isRequestAccepted = bookRequestService.acceptRequest(requestId);
        return ResponseEntity.ok(isRequestAccepted);
    }

    @GetMapping("/decline-request")
    // working // edited for front-end
    protected ResponseEntity<Boolean> declineRequest(@Param("requestId") Long requestId) {
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

    @GetMapping("/enableDisable")
    //WORKING // changed for frontend
    protected ResponseEntity<Boolean> enableDisableBook(@RequestParam(value = "id") Long id) throws BookIdNotFoundException {
        System.out.println("Inside Controller"+id);
        boolean isBookDisabled=bookService.enableDisableBook(id);
        System.out.println("Book status: "+isBookDisabled);
        return ResponseEntity.ok(isBookDisabled);
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

    @GetMapping("/last-10-book-requests")
    public ResponseEntity<List<ManageBookRequestDTO>> getLast10BookRequests() {
        List<ManageBookRequestDTO> last10Requests = bookRequestService.getLast10BookRequests();
        return ResponseEntity.ok(last10Requests);
    }
    @GetMapping("/search-requests")
    public ResponseEntity<List<ManageBookRequestDTO>> searchRequestsByUsername(@Param("username") String username) {
        List<ManageBookRequestDTO> requests = bookRequestService.searchRequestsByUsername(username);
        return ResponseEntity.ok(requests);
    }

}



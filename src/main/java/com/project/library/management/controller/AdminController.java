package com.project.library.management.controller;

import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.library.management.dto.PublisherDTO;

import com.project.library.management.service.PublisherService;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@RequiredArgsConstructor
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

    @PostMapping("/addBook")
    // working
    protected ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) throws PublisherNotFoundException {
        System.out.println("bookDTO: " + bookDTO);
        bookService.addBook(bookDTO);
        return ResponseEntity.ok("book is added successfully");
    }

    @PostMapping("/enableDisable/{id}")
    protected ResponseEntity<String> enableDisableBook(@PathVariable("id") Long id) throws BookIdNotFoundException{
        boolean isBookDisabled=bookService.enableDisableBook(id);
        return ResponseEntity.ok(isBookDisabled ?"Book Is Disabled" :"Book Is Enabled");
    }



    @PostMapping("/publisher")
    protected ResponseEntity<String> addPublisher(@RequestBody PublisherDTO publisherDTO){

        publisherService.addPublisher(publisherDTO);

        return ResponseEntity.ok("Publisher is added Successfully!");
    }

    @GetMapping("/publisher")
    protected ResponseEntity<PublisherDTO> getPublisher(@Param("id") Long id){

        PublisherDTO publisherDTO = publisherService.getPublisher(id);

        return ResponseEntity.of(Optional.of(publisherDTO));
    }

    @PutMapping("/publisher")
    protected ResponseEntity<String> updatePublisher(@RequestBody PublisherDTO publisherDTO, @Param("id") Long id){

        publisherService.updatePublisher(publisherDTO,id);

        return ResponseEntity.ok("Publisher is updated successfully!");
    }

    @DeleteMapping("/publisher")
    protected ResponseEntity<String> deletePublisher(@RequestParam Long publisherId){
        System.out.println("Id "+publisherId);
        publisherService.deletePublisher(publisherId);

        return ResponseEntity.ok("Publisher with id "+publisherId+" is deleted successfully!");
    }
}

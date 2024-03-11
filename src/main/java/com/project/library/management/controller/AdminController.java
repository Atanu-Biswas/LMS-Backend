package com.project.library.management.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.library.management.dto.BookDTO;
import com.project.library.management.exception.PublisherNotFoundException;
import com.project.library.management.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
@RequestMapping("/admin")

public class AdminController {
    private final BookService bookService;

    @GetMapping("/test")
    protected ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from admin only url");
    }

  
    

    @PostMapping("/addBook")
    protected ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) throws PublisherNotFoundException {
     boolean isbookAddedSuccessfully= bookService.addBook(bookDTO);
      return ResponseEntity.ok("book is added successfully");
    }

    @GetMapping("/getBookByName/{name}" )
    protected ResponseEntity<BookDTO> getBookByName(@PathVariable("name") String name){
       BookDTO book=bookService.getBookByName(name);
       return ResponseEntity.of(Optional.of(book));
    }
    
}

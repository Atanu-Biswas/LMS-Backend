package com.project.library.management.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.library.management.dto.ErrorDTO;
import com.project.library.management.exception.AuthorNotFoundException;
import com.project.library.management.exception.BookIdNotFoundException;
import com.project.library.management.exception.BookNotFoundException;
import com.project.library.management.exception.PublisherNotFoundException;

@Controller
@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(value = PublisherNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidCredentialsException() {
        return new ResponseEntity<>(
                ErrorDTO
                        .builder()
                        .message("Publisher Not Found Exception")
                        .timestamp(new Date())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BookNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidBookNameException() {
        return new ResponseEntity<>(
                ErrorDTO
                        .builder()
                        .message("Book With This Name Not Found")
                        .timestamp(new Date())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthorNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidAuthorNameException() {
        return new ResponseEntity<>(
                ErrorDTO
                        .builder()
                        .message("Enter Corret Author Name")
                        .timestamp(new Date())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BookIdNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidBookIdException() {
        return new ResponseEntity<>(
                ErrorDTO
                        .builder()
                        .message("Enter Corret Book Id")
                        .timestamp(new Date())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }
}

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
import com.project.library.management.exception.RequestNotFoundException;

@Controller
@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(value = PublisherNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidPublisherCredentialsException(PublisherNotFoundException e) {
        return new ResponseEntity<>(
                ErrorDTO
                        .builder()
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BookNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidBookNameException(BookNotFoundException e) {
        return new ResponseEntity<>(
                ErrorDTO
                        .builder()
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AuthorNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidAuthorNameException(AuthorNotFoundException e) {
        return new ResponseEntity<>(
                ErrorDTO
                        .builder()
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BookIdNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidBookIdException(BookIdNotFoundException e) {
        return new ResponseEntity<>(
                ErrorDTO
                        .builder()
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = RequestNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidRequestIdException(RequestNotFoundException e) {
        return new ResponseEntity<>(
                ErrorDTO
                        .builder()
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build(),
                HttpStatus.NOT_FOUND);
    }
}

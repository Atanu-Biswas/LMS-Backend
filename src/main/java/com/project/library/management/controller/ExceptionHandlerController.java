package com.project.library.management.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.library.management.dto.ErrorDTO;
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
                HttpStatus.BAD_REQUEST
            );
    }
}

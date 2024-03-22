package com.project.library.management.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.project.library.management.dto.ErrorDTO;
import com.project.library.management.exception.LMSException;

@Controller
@ControllerAdvice
public class ExceptionHandlerController {

        @ExceptionHandler(LMSException.class)
        protected ResponseEntity<ErrorDTO> handleLMSException(LMSException e) {
                        return new ResponseEntity<>(
                        ErrorDTO
                                .builder()
                                .message(e.getMessage())
                                .timestamp(new Date())
                                .build(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
        }
}

package com.project.library.management.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.library.management.service.BookRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BookRequestService bookRequestService;
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
}

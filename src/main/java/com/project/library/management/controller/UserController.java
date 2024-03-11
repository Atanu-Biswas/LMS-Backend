package com.project.library.management.controller;

import com.project.library.management.dto.BookRequestDTO;
import com.project.library.management.service.BookRequestService;
import com.project.library.management.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BookRequestService bookRequestService;
    @GetMapping("/test")
    protected ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from user only url");
    }
    @GetMapping("/is-username-available")
    protected ResponseEntity<Boolean> isUsernameAvailable(@Param("username") String username) {
        return ResponseEntity.ok(userService.isUsernameAvailable(username));
    }
    @PostMapping("/issue-request")
    public ResponseEntity<String> issueBookRequest(@RequestBody BookRequestDTO bookRequestDTO, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        boolean isIssueSuccessful = bookRequestService.issueRequest(username, bookRequestDTO);
        return ResponseEntity.ok(isIssueSuccessful ? "Book Requested Successfully!!!" : "Book Request Unsuccessfull!!!");
    }
    @GetMapping("/cancel-request")
    public ResponseEntity<String> declineBookRequest(@Param("requestId") Long requestId){
        boolean isRequestCancelled = bookRequestService.cancelRequest(requestId);
        return ResponseEntity.ok(isRequestCancelled ? "Book Request Cancelled!!!" : "Book Request Retained!!!");

    }
}

package com.project.library.management.controller;

import com.project.library.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/test")
    protected ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from user only url");
    }
    @GetMapping("/is-username-available")
    protected ResponseEntity<Boolean> isUsernameAvailable(@Param("username") String username) {
        return ResponseEntity.ok(userService.isUsernameAvailable(username));
    }

}

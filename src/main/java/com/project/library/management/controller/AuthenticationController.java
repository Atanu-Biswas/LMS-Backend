package com.project.library.management.controller;

import com.project.library.management.dto.LoginDTO;
import com.project.library.management.dto.RefreshTokenRequestDTO;
import com.project.library.management.dto.Temp;
import com.project.library.management.dto.UserDTO;
import com.project.library.management.entity.AuthenticationResponse;
import com.project.library.management.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin("http://localhost:4200")
@RequestMapping("/auth")

public class AuthenticationController {

	private final AuthenticationService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody UserDTO request) {
                
		return ResponseEntity.ok(authService.register(request));
	}

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;

    }







    @PostMapping("/login")
    public ResponseEntity<Temp> login(
            @RequestBody LoginDTO request
    ) {
        Temp temp = new Temp();
        temp.setAuthenticationResponse(authService.authenticate(request));
        temp.setRole(authService.getUserRole(request.getUsername()));
        return ResponseEntity.ok(temp);

    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody RefreshTokenRequestDTO request
    ) {
        AuthenticationResponse authenticationResponse = authService.refreshToken(request);
        return ResponseEntity.ok(authenticationResponse);
    }


}

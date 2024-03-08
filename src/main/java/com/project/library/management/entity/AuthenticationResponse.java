package com.project.library.management.entity;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private String refreshToken;
    private String role;


    public AuthenticationResponse(String token, String refreshToken, String role) {
        this.token = token;
        this.refreshToken = refreshToken;
    }


}

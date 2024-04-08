package com.project.library.management.service;

import com.project.library.management.dto.LoginDTO;
import com.project.library.management.dto.RefreshTokenRequestDTO;
import com.project.library.management.dto.UserDTO;
import com.project.library.management.entity.AuthenticationResponse;
import com.project.library.management.exception.AddressLengthException;
import com.project.library.management.exception.IllegalMobileNoException;
import com.project.library.management.exception.InvalidCredentialException;
import com.project.library.management.exception.MobileNoExistsException;
import com.project.library.management.exception.NameLengthException;
import com.project.library.management.exception.PasswordLengthException;
import com.project.library.management.exception.RefreshTokenNotFound;
import com.project.library.management.exception.UserNameExistsExecption;
import com.project.library.management.exception.UsernameLengthException;

public interface AuthenticationService {
    AuthenticationResponse register(UserDTO request) throws PasswordLengthException, MobileNoExistsException, UserNameExistsExecption, IllegalMobileNoException, AddressLengthException, UsernameLengthException, NameLengthException;

    AuthenticationResponse authenticate(LoginDTO request) throws InvalidCredentialException;

    boolean usernameExists(String usernameToCheck);

    AuthenticationResponse refreshToken(RefreshTokenRequestDTO request) throws RefreshTokenNotFound;

    String getUserRole(String username);

    void logout(String username, String Token) throws RefreshTokenNotFound;
}

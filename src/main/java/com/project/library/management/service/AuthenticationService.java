package com.project.library.management.service;

import com.project.library.management.dto.LoginDTO;
import com.project.library.management.dto.RefreshTokenRequestDTO;
import com.project.library.management.dto.UserDTO;
import com.project.library.management.entity.AuthenticationResponse;
import com.project.library.management.entity.RefreshToken;
import com.project.library.management.entity.Role;
import com.project.library.management.entity.User;
import com.project.library.management.exception.*;
import com.project.library.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger logger = Logger.getLogger(AuthenticationService.class);
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserDTO request) throws PasswordLengthException, MobileNoExistsException, UserNameExistsExecption{
        if (request.getPassword().length() < 8 || request.getPassword().length() > 20) {
            throw new PasswordLengthException("Password length should be between 8 and 20 characters.");
        }

        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserNameExistsExecption("Username already exists");
        }

        if(userRepository.existsByMobileNo(request.getMobileNo())){
            throw new MobileNoExistsException("Mobile Number already exists");
        }

        if(request.getMobileNo().length()!=10){
            throw new IllegalMobileNoException();
        }

        if(request.getAddress().length()!=30){
            throw new AddressLengthException();
        }




        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setMobileNo(request.getMobileNo());


        user.setRole(Role.USER);
        user = userRepository.save(user);
        logger.debug("Registered a new user into our LMS Portal !");
        String token = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        String refreshTokenToken = refreshToken.getToken();

        return new AuthenticationResponse(token, refreshTokenToken);
    }

    public AuthenticationResponse authenticate(LoginDTO request) throws InvalidCredentialException {
        System.out.println("Authenticate Service");
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()

                    )

            );}
        catch (Exception e){
            throw new InvalidCredentialException("Invalid Credentials!");
        }

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->
                new InvalidCredentialException("User not found")
        );
        logger.debug("Query fired for finding the Username for logging in");
        String token = jwtService.generateToken(user);
        logger.info("Generated the Access Token for the user");
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        logger.info("Generated the Refresh Token for the user");
        String refreshTokenToken = refreshToken.getToken();

        return new AuthenticationResponse(token, refreshTokenToken);

    }


	public boolean usernameExists(String usernameToCheck) {
		// TODO Auto-generated method stub
		return false;
	}





    public AuthenticationResponse refreshToken(RefreshTokenRequestDTO request) throws RefreshTokenNotFound {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken()).orElseThrow(
                () -> new RuntimeException("Refresh token is not in database!")
        );
        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, request.getRefreshToken());
    }



    public String getUserRole(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        logger.debug("Query fired for finding the User by username");
        return user.getRole().name();
    }


    public void logout(String username, String Token) throws RefreshTokenNotFound {

     RefreshToken refreshToken = refreshTokenService.findByToken(Token).orElseThrow(
                () -> new RuntimeException("Refresh token is not in database!")
        );

        if(refreshToken.getUser().getUsername().equals(username)){
                refreshTokenService.deleteByToken(Token);
        }
        else {
            throw new RuntimeException("Token not belongs to the user");
        }
}
}

package com.project.library.management.service;

import com.project.library.management.dto.LoginDTO;
import com.project.library.management.dto.UserDTO;
import com.project.library.management.entity.AuthenticationResponse;
import com.project.library.management.entity.RefreshToken;
import com.project.library.management.entity.Role;
import com.project.library.management.entity.User;
import com.project.library.management.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(RefreshTokenService refreshTokenService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }





    public AuthenticationResponse register(UserDTO request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setMobileNo(request.getMobileNo());


        user.setRole(Role.USER);
        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        String refreshTokenToken = refreshToken.getToken();

        return new AuthenticationResponse(token,refreshTokenToken);
    }
    public AuthenticationResponse authenticate(LoginDTO request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()

                )
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        String token = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        String refreshTokenToken = refreshToken.getToken();

        return new AuthenticationResponse(token,refreshTokenToken,);

    }
}

package com.project.library.management.service;

import com.project.library.management.dto.LoginDTO;
import com.project.library.management.dto.RefreshTokenRequestDTO;
import com.project.library.management.dto.UserDTO;
import com.project.library.management.entity.AuthenticationResponse;
import com.project.library.management.entity.RefreshToken;
import com.project.library.management.entity.Role;
import com.project.library.management.entity.User;
import com.project.library.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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

        return new AuthenticationResponse(token, refreshTokenToken);
    }

    public AuthenticationResponse authenticate(LoginDTO request) {

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

        return new AuthenticationResponse(token, refreshTokenToken);

    }


	public boolean usernameExists(String usernameToCheck) {
		// TODO Auto-generated method stub
		return false;
	}





    public AuthenticationResponse refreshToken(RefreshTokenRequestDTO request) {
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
        return user.getRole().name();
    }


}

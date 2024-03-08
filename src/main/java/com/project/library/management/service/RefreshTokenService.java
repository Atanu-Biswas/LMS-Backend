package com.project.library.management.service;

import com.project.library.management.entity.RefreshToken;
import com.project.library.management.repository.RefreshTokenRepository;
import com.project.library.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(username).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(30L *24*60*60*1000))
                .build();

        return refreshTokenRepository.save(refreshToken);

    }

    private Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken validateRefreshToken( RefreshToken token) {
       if(token.getExpiryDate().isBefore(Instant.now())){
           refreshTokenRepository.delete(token);
           throw new RuntimeException(token.getToken()+"refresh Token Expired");
       }
       return token;
    }
}

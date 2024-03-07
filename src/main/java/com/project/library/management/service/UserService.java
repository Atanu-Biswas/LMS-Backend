package com.project.library.management.service;

import com.project.library.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
}

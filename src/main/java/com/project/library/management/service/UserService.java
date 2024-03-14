package com.project.library.management.service;

import com.project.library.management.dto.UserDTO;
import com.project.library.management.entity.User;
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

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(null);

        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setAddress(user.getAddress());
        userDTO.setMobileNo(user.getMobileNo());
        return userDTO;
    }
}

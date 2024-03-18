package com.project.library.management.service;

import com.project.library.management.dto.UserDTO;
import com.project.library.management.dto.UserPaginationDTO;
import com.project.library.management.entity.User;
import com.project.library.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setAddress(user.getAddress());
        userDTO.setMobileNo(user.getMobileNo());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole().toString());
        return userDTO;
    }

    public UserPaginationDTO getAllUsers(int pageNumber,int pageSize,String sortBy,String sortDir){

        Sort sort= (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> userPage= userRepository.findAll(p);

        List<User> users = userPage.getContent();

        List<UserDTO> userDTOs=users.stream().map(user->{
            UserDTO userDTO=convertToDTO(user);
            return userDTO;
        }).collect(Collectors.toList());

        UserPaginationDTO userPaginationDTO = new UserPaginationDTO();
        userPaginationDTO.setContent(userDTOs);
        userPaginationDTO.setPageNumber(pageNumber);
        userPaginationDTO.setPageSize(pageSize);
        userPaginationDTO.setTotalElements(userPage.getTotalElements());
        userPaginationDTO.setTotalPages(userPage.getTotalPages());
        userPaginationDTO.setLastPage(userPage.isLast());

        return userPaginationDTO;
    }
}

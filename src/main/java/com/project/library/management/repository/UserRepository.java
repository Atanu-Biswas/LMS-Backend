package com.project.library.management.repository;

import com.project.library.management.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);

    Page<User> findByUsernameStartingWithIgnoreCase(String username,Pageable p);
}


package com.project.library.management.repository;

import com.project.library.management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);

    Page<User> findByUsernameStartingWithIgnoreCase(String username,Pageable p);
    @Query("SELECT COUNT(u) FROM User u")
    int getTotalUsersAvailable();


}


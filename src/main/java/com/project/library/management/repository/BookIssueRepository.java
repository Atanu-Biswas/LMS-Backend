package com.project.library.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.library.management.entity.BookIssue;

public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
    @Query("SELECT bi FROM BookIssue bi WHERE bi.user.username = :username")
    List<BookIssue> findByUsername(String username);
}

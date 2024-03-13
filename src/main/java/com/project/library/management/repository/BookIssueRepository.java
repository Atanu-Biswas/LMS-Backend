package com.project.library.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.library.management.entity.BookIssue;

public interface BookIssueRepository extends JpaRepository<BookIssue,Long>  {
    
}

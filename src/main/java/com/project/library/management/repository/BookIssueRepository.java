package com.project.library.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.library.management.entity.BookIssue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookIssueRepository extends JpaRepository<BookIssue, Integer>{
    @Query("SELECT b FROM BookIssue b WHERE b.user.username=:username AND b.fineStatus='UNPAID'")
    List<BookIssue> calculateFinesByUserName(@Param("username") String username);
}

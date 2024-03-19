package com.project.library.management.repository;

import com.project.library.management.entity.BookIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookIssueRepository extends JpaRepository<BookIssue, Long>{
    @Query("SELECT b FROM BookIssue b WHERE b.user.username=:username AND b.fineStatus='UNPAID'")
    List<BookIssue> calculateFinesByUserName(@Param("username") String username);

    Page<BookIssue> findByUserUsername(String username, Pageable p);

}

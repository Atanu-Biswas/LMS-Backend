package com.project.library.management.repository;

import com.project.library.management.entity.BookIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public interface BookIssueRepository extends JpaRepository<BookIssue, Long>{
    @Query("SELECT b FROM BookIssue b WHERE b.user.username=:username AND b.fineStatus='UNPAID'")
    Page<BookIssue> calculateFinesByUserName(String username,Pageable p);

    Page<BookIssue> findByUserUsername(String username, Pageable p);
    
    @Query("SELECT COUNT(bi) > 0 FROM BookIssue bi WHERE bi.book.bookId = :bookId")
    boolean existsByBookId(Long bookId);

    @Query(value = "SELECT u.username, COUNT(*) as booksBorrowed " +
            "FROM BookIssue bi " +
            "JOIN User u ON bi.user.userId = u.userId " +
            "GROUP BY u.username " +
            "ORDER BY booksBorrowed DESC"
            )
    List<Object[]> findUserActivity();
}

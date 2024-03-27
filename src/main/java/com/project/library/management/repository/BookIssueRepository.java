package com.project.library.management.repository;

import com.project.library.management.entity.BookIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface BookIssueRepository extends JpaRepository<BookIssue, Long>{
    @Query("SELECT b FROM BookIssue b WHERE b.user.username=:username AND b.fineStatus='UNPAID'")
    Page<BookIssue> calculateFinesByUserName(String username,Pageable p);

    Page<BookIssue> findByUserUsername(String username, Pageable p);
    
    @Query("SELECT COUNT(bi) > 0 FROM BookIssue bi WHERE bi.book.bookId = :bookId")
    boolean existsByBookId(Long bookId);
    @Query("SELECT bi.book, COUNT(bi) AS issueCount " +
            "FROM BookIssue bi " +
            "WHERE bi.issueDate BETWEEN :startDate AND :endDate " +
            "GROUP BY bi.book " +
            "ORDER BY issueCount DESC")
    List<Object[]> findByIssueDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);



}

package com.project.library.management.repository;

import com.project.library.management.entity.BookIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BookIssueRepository extends JpaRepository<BookIssue, Long>{
//    @Query("SELECT b FROM BookIssue b WHERE b.user.username=:username AND b.fineStatus='UNPAID'")
    @Query("SELECT b FROM BookIssue b WHERE b.user.username=:username AND b.fineStatus='UNPAID'")
    Page<BookIssue> calculateFinesByUserName(String username,Pageable p);

    Page<BookIssue> findByUserUsername(String username, Pageable p);
    
    @Query("SELECT COUNT(bi) > 0 FROM BookIssue bi WHERE bi.book.bookId = :bookId")
    boolean existsByBookId(Long bookId);
}

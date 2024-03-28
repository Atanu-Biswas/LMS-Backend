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
//    @Query("SELECT b FROM BookIssue b WHERE b.user.username=:username AND b.fineStatus='UNPAID'")
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


    @Query(value = "SELECT b.title, COUNT(bi.issueId) AS issueCount " +
            "FROM BookIssue bi " +
            "JOIN bi.book b " +
            "GROUP BY b.title " +
            "ORDER BY issueCount DESC " +
            "FETCH NEXT 5 ROWS ONLY")
    List<Object[]> getMostRequestedBooks();

    @Query("SELECT b.author, COUNT(bi) " +
            "FROM BookIssue bi " +
            "JOIN bi.book b " +
            "GROUP BY b.author " +
            "ORDER BY COUNT(bi) DESC " +
            "FETCH FIRST 5 ROWS ONLY")
    List<Object[]> findTopAuthorsWithBooksIssued();

    @Query("SELECT SUM(CASE WHEN bi.returnDate IS NOT NULL THEN bi.fine ELSE 0 END), " +
            "SUM(CASE WHEN bi.returnDate IS NULL THEN bi.fine ELSE 0 END) " +
            "FROM BookIssue bi")
    List<Object[]> getTotalFinesPaidAndDue();

}

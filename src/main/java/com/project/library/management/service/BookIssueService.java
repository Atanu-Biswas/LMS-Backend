package com.project.library.management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.library.management.dto.BookIssueDTO;
import com.project.library.management.entity.BookIssue;
import com.project.library.management.repository.BookIssueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookIssueService {
    private final BookIssueRepository bookIssueRepository;

    public List<BookIssueDTO> viewBorrowedBooks(String username) {

        List<BookIssue> bookIssues = bookIssueRepository.findByUsername(username);
        List<BookIssueDTO> borrowedBooks = new ArrayList<>();

        bookIssues.forEach(issue -> {
            BookIssueDTO bookIssueDTO = new BookIssueDTO();

            bookIssueDTO.setIssueId(issue.getIssueId());
            bookIssueDTO.setBookId(issue.getBook().getBookId());
            bookIssueDTO.setUserId(issue.getUser().getUserId());
            bookIssueDTO.setIssueDate(issue.getIssueDate());
            bookIssueDTO.setDueDate(issue.getIssueDate());
            bookIssueDTO.setReturnDate(issue.getReturnDate());
            bookIssueDTO.setFine(issue.getFine());

            borrowedBooks.add(bookIssueDTO);
        });

        return borrowedBooks;
    }

}

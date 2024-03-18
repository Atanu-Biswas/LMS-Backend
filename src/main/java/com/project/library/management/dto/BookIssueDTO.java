package com.project.library.management.dto;

import com.project.library.management.entity.Book;
import com.project.library.management.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookIssueDTO {

    private Long issueId;

    private Long bookId;

    private String userId;

    private Book book;

    private User user;

    private Date issueDate;

    private Date dueDate;

    private Date returnDate;

    private String fine = "0";

}


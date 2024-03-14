package com.project.library.management.dto;

import java.util.Date;

import com.project.library.management.entity.Book;
import com.project.library.management.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class BookIssueDTO {

    private Long issueId;

    private Book book;

    private User user;

    private Date issueDate;

    private Date dueDate;

    private Date returnDate;

    private String fine = "0";

}

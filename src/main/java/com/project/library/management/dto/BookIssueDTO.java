package com.project.library.management.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookIssueDTO {

    private Long issueId;

    private Long bookId;

    private String userId;

    private Date issueDate;

    private Date dueDate;

    private Date returnDate;

    private String fine = "0";

}

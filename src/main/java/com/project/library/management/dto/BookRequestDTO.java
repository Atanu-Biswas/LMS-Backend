package com.project.library.management.dto;

import java.util.Date;
import java.util.UUID;

import com.project.library.management.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO 
{
    private Long bookId;
    private int weeksRequested;
}

package com.project.library.management.service;

import com.project.library.management.entity.BookIssue;
import com.project.library.management.repository.BookIssueRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Date;


@Component
public class FineService {
    BookIssueRepository bookIssueRepository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailyTask() {
        List<BookIssue> bookIssueList = bookIssueRepository.findAll();

        Date currentDate = new Date(); // Set current date

        bookIssueList.forEach(bookIssue -> {
            if (bookIssue.getDueDate().before(currentDate)) {
                long diff = currentDate.getTime() - bookIssue.getDueDate().getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                bookIssue.setFine((int) diffDays * 10);
            }
        });
        bookIssueRepository.saveAll(bookIssueList);
    }
}
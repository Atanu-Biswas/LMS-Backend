package com.project.library.management.service;
import com.project.library.management.dto.AdminDashboardDTO;
import com.project.library.management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
public class AdminDashboardService {

    @Autowired
    private BookRepository booksRepository;

    public AdminDashboardDTO getGenreWiseSplit() {
        List<Object[]> genreCounts = booksRepository.getGenreWiseSplit();

        Map<String, Long> genreWiseSplit = genreCounts.stream()
                .collect(Collectors.toMap(
                        array -> (String) array[0], // Genre
                        array -> (Long) array[1]   // Count
                ));

        AdminDashboardDTO dashboardDTO = new AdminDashboardDTO();
        dashboardDTO.setGenreWiseSplit(genreWiseSplit);
        return dashboardDTO;
    }
}


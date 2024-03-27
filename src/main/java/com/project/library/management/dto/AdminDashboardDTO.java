package com.project.library.management.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;
@Data
@RequiredArgsConstructor
public class AdminDashboardDTO {
    private Map<String, Long> genreWiseSplit;

}

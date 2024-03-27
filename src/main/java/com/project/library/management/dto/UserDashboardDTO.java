package com.project.library.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
@RequiredArgsConstructor
@AllArgsConstructor
@Data

public class UserDashboardDTO {
    private  String title;
    private String author;
    private HashMap<String,Long> inDemand;
}

package com.project.library.management.entity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Genre {

    SCIENCE_FICTION("SCI-FI"),
    SELF_HELP("SELF_HELP"),
    THRILLER("THRILLER"),
    EDUCATIONAL("EDUCATIONAL"),
    HISTORICAL_FICTION("HISTORICAL_FICTION"),
    OTHERS("OTHERS");
    private final String Genre;

}

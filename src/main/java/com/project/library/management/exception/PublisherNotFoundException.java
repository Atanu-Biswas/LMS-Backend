package com.project.library.management.exception;

public class PublisherNotFoundException extends Exception {
    public PublisherNotFoundException() {
        super("Publisher not found!!!");
    }
}
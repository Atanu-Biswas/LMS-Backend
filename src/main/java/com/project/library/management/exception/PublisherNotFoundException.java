package com.project.library.management.exception;

public class PublisherNotFoundException extends Exception{
    public PublisherNotFoundException() {
        super("Publisher Not Found Exception");
    }

    public PublisherNotFoundException(String message) {
        super(message);
    }
}

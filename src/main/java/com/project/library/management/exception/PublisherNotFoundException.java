package com.project.library.management.exception;

public class PublisherNotFoundException extends RuntimeException{
    public PublisherNotFoundException() {
        super("Publisher Not Found Exception");
    }
    public PublisherNotFoundException(String message) {
        super(message);
    }


}

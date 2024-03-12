package com.project.library.management.exception;

public class BookIdNotFoundException extends Exception {
    public BookIdNotFoundException(){
        super("Book Id Not Found!!");
    }

    public BookIdNotFoundException(String message) {
        super(message);
    }
}

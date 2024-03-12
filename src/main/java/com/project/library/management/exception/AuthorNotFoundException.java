package com.project.library.management.exception;

public class AuthorNotFoundException extends Exception {
    public AuthorNotFoundException(){
        super("Book Not Found!!");
    }

    public AuthorNotFoundException(String message) {
        super(message);
    }
}

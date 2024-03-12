package com.project.library.management.exception;

public class BookNotFoundException  extends Exception{
    public BookNotFoundException(){
        super("Book Not Found!!");
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}

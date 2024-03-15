package com.project.library.management.exception;

public class RequestNotFoundException extends Exception{

        public RequestNotFoundException(){
            super();
        }
    
        public RequestNotFoundException(String message) {
            super(message);
        }
}

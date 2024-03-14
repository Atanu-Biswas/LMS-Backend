package com.project.library.management.exception;

public class SQLException extends Exception{
    
    public SQLException(){
        super("SQL Exception occured!!");
    }

    public SQLException(String message){
        super(message);
    }
}

package com.project.library.management.exception;

public class IllegalQuantityException extends LMSException{
    public IllegalQuantityException(){
        super("Quantity should be greater than 0.");
    }

    public IllegalQuantityException(String message) {
        super(message);
    }
}

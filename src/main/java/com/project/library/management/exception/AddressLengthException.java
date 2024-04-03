package com.project.library.management.exception;

public class AddressLengthException extends LMSException {
    public AddressLengthException() {
        super("Address should be less than 30 characters");
    }
}

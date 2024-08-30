package com.example.moneytransferapi.exception;

public class InvalidUserDataException extends RuntimeException{
    public InvalidUserDataException(String message) {
        super(message);
    }
}

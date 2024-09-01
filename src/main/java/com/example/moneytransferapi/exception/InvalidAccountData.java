package com.example.moneytransferapi.exception;

public class InvalidAccountData extends RuntimeException{
    public InvalidAccountData(String message) {
        super(message);
    }
}

package com.example.moneytransferapi.exception;

public class ExpiredRefreshTokenException extends RuntimeException{
    public ExpiredRefreshTokenException(String message) {
        super(message);
    }
}

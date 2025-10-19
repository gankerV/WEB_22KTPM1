package com.example.APIDemo.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
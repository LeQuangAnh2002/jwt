package com.example.usersmanagementsystem.exception;

public class UnauthorizedAccessException extends Exception{
    public UnauthorizedAccessException(String message){
        super(message);
    }
}

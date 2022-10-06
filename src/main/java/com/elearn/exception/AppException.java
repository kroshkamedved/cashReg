package com.elearn.exception;

public class AppException extends Exception {
    public AppException(String ex, Throwable ex1) {
        super(ex, ex1);
    }
}

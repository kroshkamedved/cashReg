package com.elearn.fp.exception;

/**
 * Custom DB exception.
 */
public class DBException extends AppException {
    public DBException(String ex, Throwable ex1) {
        super(ex, ex1);
    }
}

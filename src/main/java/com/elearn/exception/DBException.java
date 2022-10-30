package com.elearn.exception;

import com.elearn.exception.AppException;

import java.sql.SQLException;

public class DBException extends AppException {
    public DBException(String ex, Throwable ex1) {
        super(ex, ex1);
    }
}

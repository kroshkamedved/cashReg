package com.elearn.command;

import com.elearn.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    void initContext();
    String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException;
}

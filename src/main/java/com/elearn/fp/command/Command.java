package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command interface for main controller.
 */
public interface Command {
    default void initContext() {
    }

    String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException;
}

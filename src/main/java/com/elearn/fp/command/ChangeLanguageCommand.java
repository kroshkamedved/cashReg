package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeLanguageCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        String loc = req.getParameter("loc");
        req.getSession().setAttribute("loc", loc);
        return ((String) req.getSession().getAttribute("lastPage"));
    }
}

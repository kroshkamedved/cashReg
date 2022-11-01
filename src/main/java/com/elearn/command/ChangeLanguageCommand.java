package com.elearn.command;

import com.elearn.exception.AppException;
import com.elearn.logic.ProductManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeLanguageCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        String loc = req.getParameter("loc");
        req.getSession().setAttribute("loc", loc);
        return "/fp/";
    }
}

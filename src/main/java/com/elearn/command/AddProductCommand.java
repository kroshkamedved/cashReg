package com.elearn.command;

import com.elearn.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class AddProductCommand implements Command {
    @Override
    public void initContext() {

    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        String productName = req.getParameter("prod_name");
        return productName;
    }
}

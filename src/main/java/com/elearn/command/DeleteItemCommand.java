package com.elearn.command;

import com.elearn.exception.AppException;
import com.elearn.logic.ProductManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteItemCommand implements Command {
    @Override
    public void initContext() {

    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        ProductManager pm = ProductManager.getInstance();
        pm.deleteProduct(req);
        return "cabinet/commodity_expert_page";
    }
}

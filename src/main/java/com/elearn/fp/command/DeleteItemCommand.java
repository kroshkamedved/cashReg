package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;
import com.elearn.fp.logic.ProductManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteItemCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        ProductManager pm = ProductManager.getInstance();
        pm.deleteProduct(req);
        return "cabinet/commodity_expert_page";
    }
}

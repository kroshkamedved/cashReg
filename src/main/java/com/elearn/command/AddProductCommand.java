package com.elearn.command;

import com.elearn.exception.DBException;
import com.elearn.logic.ProductManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        ProductManager pm = ProductManager.getInstance();
        pm.createProduct(req);
        return "cabinet/commodity_expert_page/addedItemPage";
    }
}

package com.elearn.fp.command;

import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.ProductManager;

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

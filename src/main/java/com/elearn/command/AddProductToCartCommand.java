package com.elearn.command;

import com.elearn.db.DBException;
import com.elearn.logic.ProductManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductToCartCommand implements Command {
    @Override
    public void initContext() {

    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        ProductManager pm = ProductManager.getInstance();
        pm.addProductToCart(req);
        return "cabinet/cashier_page";
    }


}

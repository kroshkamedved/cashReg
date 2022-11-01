package com.elearn.command;

import com.elearn.exception.AppException;
import com.elearn.exception.DBException;
import com.elearn.logic.ProductManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteItemFromOrderCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        ProductManager pm = ProductManager.getInstance();
        try {
            pm.deleteItemFromOrder(req);
        } catch (DBException e) {
            throw new AppException(e.getMessage(), e);
        }
        return "/fp/cabinet/cashier_page/checks";
    }
}

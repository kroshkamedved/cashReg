package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;
import com.elearn.fp.exception.DBException;
import com.elearn.fp.logic.ProductManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteWholeOrderCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        ProductManager pm = ProductManager.getInstance();
        try {
            pm.deleteWholeOrder(req);
        } catch (DBException e) {
            throw new AppException(e.getMessage(), e);
        }
        return "/fp/cabinet/cashier_page/checks";
    }
}

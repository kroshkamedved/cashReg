package com.elearn.command;

import com.elearn.db.DBException;
import com.elearn.db.entity.ItemDTO;
import com.elearn.exception.AppException;
import com.elearn.logic.ProductManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductCommand implements Command {
    @Override
    public void initContext() {

    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        ProductManager pm = ProductManager.getInstance();
        pm.createProduct(req);
        return "cabinet/commodity_expert_page/addedItemPage";
    }


}

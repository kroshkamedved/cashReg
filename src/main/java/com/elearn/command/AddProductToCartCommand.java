package com.elearn.command;

import com.elearn.exception.DBException;
import com.elearn.logic.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductToCartCommand implements Command {

    Logger logger = LogManager.getLogger(AddProductToCartCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        ProductManager pm = ProductManager.getInstance();
        pm.addProductToCart(req);
        logger.trace("product added to cart");
        return "cabinet/cashier_page";
    }


}

package com.elearn.fp.command;

import com.elearn.fp.exception.AppException;
import com.elearn.fp.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeStockCommand implements Command {
    Logger logger = LogManager.getLogger(ChangeStockCommand.class);

    /**
     * Change quantity of product in stock
     * @param req
     * @param resp
     * @return servlet uri
     * @throws AppException
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws AppException {
        ProductManager pm = ProductManager.getInstance();
        pm.updateProductAfterPurchase(req);
        logger.trace("stock changed");
        return "cabinet/commodity_expert_page";
    }
}

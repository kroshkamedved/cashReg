package com.elearn.fp.command;

import com.elearn.fp.exception.DBException;
import com.elearn.fp.service.ProductManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductCommand implements Command {
    Logger logger = LogManager.getLogger(AddProductCommand.class);
    /**
     * Creates new product in db.
     * @param req
     * @param resp
     * @return servlet url
     * @throws DBException
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        ProductManager pm = ProductManager.getInstance();
        pm.createProduct(req);
        logger.trace("product added to db");
        return "cabinet/commodity_expert_page/addedItemPage";
    }
}
